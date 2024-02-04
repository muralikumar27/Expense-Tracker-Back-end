package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.exception.UserAlreadyExistsException;
import com.murali.expensetracker.model.UserModel;

public interface UserRegistrationService {

    void registerUser(UserModel userModel) throws Exception;
}
