package com.murali.expensetracker.service;

import com.murali.expensetracker.model.PasswordModel;

public interface PasswordResetService {
    void resetPasswordTokenEmail(String email,String url) throws Exception;

    String ValidResetToken(String token);

    void resetPassword(PasswordModel passwordModel) throws Exception;

    boolean changePassword(PasswordModel passwordModel);
}
