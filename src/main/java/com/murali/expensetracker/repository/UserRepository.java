package com.murali.expensetracker.repository;

import com.murali.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmailId(String email);

    Optional<User>getByEmailId(String email);
}
