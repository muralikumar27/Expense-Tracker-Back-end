package com.murali.expensetracker.repository;

import com.murali.expensetracker.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification,Long> {
    UserVerification getByToken(String token);
}
