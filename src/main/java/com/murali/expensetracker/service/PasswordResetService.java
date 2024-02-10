package com.murali.expensetracker.service;

public interface PasswordResetService {
    void resetPasswordTokenEmail(String email,String url) throws Exception;
}
