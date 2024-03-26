package com.murali.expensetracker.repository;

import com.murali.expensetracker.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<JwtToken, Long> {

    @Query("""
            SELECT t FROM JwtToken t
            WHERE t.user.userId = :userId and t.revoked = false
            """)
    List<JwtToken>findValidTokens(long userId);

    Optional<JwtToken>findByToken(String token);

}
