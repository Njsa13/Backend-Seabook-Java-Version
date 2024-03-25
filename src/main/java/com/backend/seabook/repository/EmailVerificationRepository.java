package com.backend.seabook.repository;

import com.backend.seabook.enumeration.EnumEmailVerificationType;
import com.backend.seabook.model.EmailVerification;
import com.backend.seabook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findFirstByUserAndEmailVerificationType(User user, EnumEmailVerificationType emailVerificationType);
    Optional<EmailVerification> findFirstByToken(String token);
}
