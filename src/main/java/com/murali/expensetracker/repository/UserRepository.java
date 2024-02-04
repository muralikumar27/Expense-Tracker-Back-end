package com.murali.expensetracker.repository;

import com.murali.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {



    boolean existsByEmailId(String email);
}
