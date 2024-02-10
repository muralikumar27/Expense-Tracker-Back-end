package com.murali.expensetracker.controller;


import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.event.UserRegistrationEvent;
import com.murali.expensetracker.model.UserModel;
import com.murali.expensetracker.response.UserRegistrationStatus;
import com.murali.expensetracker.service.UrlCreation;
import com.murali.expensetracker.service.UserRegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserRegistrationController {

    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UrlCreation urlCreation;

    /*
    * This method handles the POST request from the user to
    * register the user account, first the user will be saved to the
    * Database, after saving user details to database with disabled account status,
    * an event will be published send email to the user to enable his account.
    * publishEvent() method from interface ApplicationEventPublisher with
    * User object and Base Url of the application as parameters.
    */
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationStatus> registerUser(@Valid @RequestBody UserModel userModel,HttpServletRequest request) throws Exception {
        User user = userRegistrationService.registerUser(userModel);
        UserRegistrationStatus userRegistrationStatus = new UserRegistrationStatus("Check email for verification link", HttpStatus.OK);
        applicationEventPublisher.publishEvent(new UserRegistrationEvent(user,urlCreation.createApplicationUrl(request)));
        return new ResponseEntity<>(userRegistrationStatus,HttpStatus.OK);
    }


    /*
    * When the user clicks on the verification link
    * the verification token will be validated by the
    * userRegistrationService.verifyUser(token) method,
    * based on the return value from the verifyUser method,
    * response will be sent to the user.
    */
    @GetMapping("/verify-user")
    public ResponseEntity<UserRegistrationStatus>verifyUser(@RequestParam("token") String token){
        String validation = userRegistrationService.verifyUser(token);
        if(validation.equals("valid")){
            UserRegistrationStatus userRegistrationStatus = new UserRegistrationStatus("Account Verified !", HttpStatus.OK);
            return new ResponseEntity<>(userRegistrationStatus,HttpStatus.OK);
        }
        else if (validation.equals("expired")) {
            UserRegistrationStatus userRegistrationStatus = new UserRegistrationStatus("Token expired !", HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(userRegistrationStatus,HttpStatus.UNAUTHORIZED);
        }
        else{
            UserRegistrationStatus userRegistrationStatus = new UserRegistrationStatus("Invalid Token !", HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(userRegistrationStatus,HttpStatus.UNAUTHORIZED);
        }

    }
}
