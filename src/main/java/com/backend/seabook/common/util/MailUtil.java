package com.backend.seabook.common.util;

import com.backend.seabook.exception.ServiceBusinessException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmailVerificationAsync(String email, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, true);
            msg.setFrom("noreply@seabook.com");
            msg.setTo(email);
            msg.setSubject(subject);
            msg.setText(text);
            msg.setSentDate(new Date());
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to send email");
        }
    }
}
