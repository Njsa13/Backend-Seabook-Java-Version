package com.backend.seabook.service;

import com.backend.seabook.enumeration.EnumEmailVerificationType;

public interface EmailService {
    void sendEmail(String email, EnumEmailVerificationType emailVerificationType);
    void verifyEmailRegister(String token);
}
