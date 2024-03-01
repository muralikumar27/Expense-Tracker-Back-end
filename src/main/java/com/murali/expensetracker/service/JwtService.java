package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.model.LoginModel;

import java.util.Date;

public interface JwtService {
    String generateToken(Long id);

    String getUserId(String token) throws Exception;

    Date getExpirationDateFromToken(String token) throws Exception;

    boolean verifyToken(String jwt);

    User getUser(String email);
}
