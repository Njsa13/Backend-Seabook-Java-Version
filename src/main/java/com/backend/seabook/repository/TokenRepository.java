package com.backend.seabook.repository;

import com.backend.seabook.model.Token;
import com.backend.seabook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query("""
            select t from Token t inner join t.User u
            where u = :user and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokensByUser(User user);

    Optional<Token> findByToken(String token);
}
