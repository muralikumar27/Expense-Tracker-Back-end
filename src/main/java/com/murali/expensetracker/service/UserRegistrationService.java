package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.model.UserModel;

public interface UserRegistrationService {

    User registerUser(UserModel userModel) throws Exception;

    void saveUserToken(User user, String token);

    String verifyUser(String token);

    String resendVerificationToken(String token, String applicationUrl);


}
