package com.murali.expensetracker.controller;


import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.exception.UserAlreadyExistsException;
import com.murali.expensetracker.model.UserModel;
import com.murali.expensetracker.response.UserRegistrationStatus;
import com.murali.expensetracker.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserRegistrationController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationStatus> registerUser(@Valid @RequestBody UserModel userModel) throws Exception {
        userRegistrationService.registerUser(userModel);
        UserRegistrationStatus userRegistrationStatus = new UserRegistrationStatus("Check email for verification link", HttpStatus.OK);
        return new ResponseEntity<>(userRegistrationStatus,HttpStatus.OK);
    }
}
