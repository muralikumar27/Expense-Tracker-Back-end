package com.murali.expensetracker.repository;

import com.murali.expensetracker.entity.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken,Long> {

    Optional<PasswordToken> getByResetToken(String token);
}
