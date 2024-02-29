package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.model.LoginModel;

public interface JwtService {
    public String generateToken(Long id);
    User verifyUser(LoginModel loginModel);
}
