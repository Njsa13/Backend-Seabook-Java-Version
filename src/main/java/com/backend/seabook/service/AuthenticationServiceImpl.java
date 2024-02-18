package com.backend.seabook.service;

import com.backend.seabook.common.util.JwtUtil;
import com.backend.seabook.dto.request.LoginRequest;
import com.backend.seabook.dto.request.RegisterRequest;
import com.backend.seabook.dto.response.LoginResponse;
import com.backend.seabook.dto.response.RefreshTokenResponse;
import com.backend.seabook.dto.response.RegisterResponse;
import com.backend.seabook.enumeration.EnumTokenAccessType;
import com.backend.seabook.enumeration.EnumTokenType;
import com.backend.seabook.exception.DataNotFoundException;
import com.backend.seabook.exception.ForbiddenException;
import com.backend.seabook.exception.ServiceBusinessException;
import com.backend.seabook.exception.UserNotActiveException;
import com.backend.seabook.model.Token;
import com.backend.seabook.model.User;
import com.backend.seabook.repository.TokenRepository;
import com.backend.seabook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.backend.seabook.common.util.Constants.ControllerMessage.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest request) {
        try {
            User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            user = userRepository.save(user);
            return RegisterResponse.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            User user = userRepository.findFirstByEmail(request.getEmail())
                    .orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    request.getPassword()
            ));
            if (!user.isVerifiedEmail()) {
                throw new UserNotActiveException(USER_NOT_VERIFIED);
            }
            String accessToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            user.setLastLogin(LocalDateTime.now());

            saveToken(accessToken, user, EnumTokenAccessType.ACCESS);
            saveToken(refreshToken, user, EnumTokenAccessType.REFRESH);
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (DataNotFoundException | UserNotActiveException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public RefreshTokenResponse refreshToken() {
        try {
            String refreshToken = jwtUtil.getTokenFromRequest();
            String email = jwtUtil.extractEmail(refreshToken);
            User user = userRepository.findFirstByEmail(email)
                    .orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
            if (jwtUtil.isTokenValid(refreshToken, user)) {
                Token token = tokenRepository.findByToken(refreshToken)
                        .orElseThrow(() -> new ServiceBusinessException("Token Doesn't Exist In Database"));
                if (!token.getTokenAccessType().equals(EnumTokenAccessType.REFRESH)) {
                    throw new ForbiddenException("Token Type Is Invalid");
                }
            }
            String accessToken = jwtUtil.generateToken(user);
            saveToken(accessToken, user, EnumTokenAccessType.ACCESS);
            return RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    private void saveToken(String jwtToken, User user, EnumTokenAccessType tokenAccessType) {
        Token token = Token.builder()
                .token(jwtToken)
                .tokenType(EnumTokenType.BEARER)
                .tokenAccessType(tokenAccessType)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }
}
