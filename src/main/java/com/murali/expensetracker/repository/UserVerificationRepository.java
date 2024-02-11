package com.murali.expensetracker.repository;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification,Long> {

    UserVerification getByToken(String token);
    Optional<UserVerification> getByUser(User user);
}
