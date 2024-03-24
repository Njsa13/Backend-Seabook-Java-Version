package com.backend.seabook.service;

import com.backend.seabook.common.util.MailUtil;
import com.backend.seabook.enumeration.EnumEmailVerificationType;
import com.backend.seabook.exception.DataNotFoundException;
import com.backend.seabook.exception.ForbiddenException;
import com.backend.seabook.exception.ServiceBusinessException;
import com.backend.seabook.model.EmailVerification;
import com.backend.seabook.model.User;
import com.backend.seabook.repository.EmailVerificationRepository;
import com.backend.seabook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.backend.seabook.common.util.Constants.ControllerMessage.*;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MailUtil mailUtil;

    @Override
    public void sendEmail(String email, EnumEmailVerificationType emailVerificationType) {
        try {
            User user = userRepository.findFirstByEmail(email)
                    .orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
            String verificationUrl = getVerificationUrl(emailVerificationType, user);
            String subject;
            String text;
            if (user.isVerifiedEmail()) {
                if (emailVerificationType.equals(EnumEmailVerificationType.REGISTER)) {
                    throw new ForbiddenException(USER_ALREADY_VERIFIED);
                } else {
                    subject = "Reset Your Password";
                    text = "Please reset your password by clicking this link: " + verificationUrl;
                }
            } else {
                if (emailVerificationType.equals(EnumEmailVerificationType.REGISTER)) {
                    subject = "Verify your email";
                    text = "Please verify your email by clicking this link: " + verificationUrl;
                } else {
                    throw new ForbiddenException(USER_NOT_VERIFIED);
                }
            }
            mailUtil.sendEmailVerificationAsync(email, subject, text);
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    public void verifyEmailRegister(String token) {
        try {
            emailVerificationRepository.findFirstByToken(token)
                    .ifPresentOrElse(val -> {
                        if (val.getExpTime().isBefore(LocalDateTime.now())) {
                            throw new ForbiddenException(TOKEN_EXPIRED);
                        } else {
                            User user = val.getUser();
                            user.setVerifiedEmail(true);
                            val.setUser(null);
                            emailVerificationRepository.delete(val);
                        }
                    }, () -> {throw new DataNotFoundException(TOKEN_NOT_FOUND);});
        } catch (ForbiddenException | DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private String getVerificationUrl(EnumEmailVerificationType emailVerificationType, User user) {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            emailVerificationRepository.findFirstByUserAndEmailVerificationType(user, emailVerificationType)
                    .ifPresent(val -> {
                        if (val.getExpTime().isAfter(currentTime)) {
                            throw new ForbiddenException("Please wait for 10 minutes to generate new token");
                        } else {
                            emailVerificationRepository.delete(val);
                        }
                    });
            String token = generateToken();
            EmailVerification emailVerification = EmailVerification.builder()
                    .token(token)
                    .emailVerificationType(emailVerificationType)
                    .expTime(currentTime.plusMinutes(10))
                    .user(user)
                    .build();
            emailVerificationRepository.save(emailVerification);
            if (emailVerificationType.equals(EnumEmailVerificationType.REGISTER)) {
                return "http://localhost:4200/email-verify-register?token=" + token;
            } else {
                return "http://localhost:4200/email-verify-forgot-password?token=" + token;
            }
        } catch (ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }
}
