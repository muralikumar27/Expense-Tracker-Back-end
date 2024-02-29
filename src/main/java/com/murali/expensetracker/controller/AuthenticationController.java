package com.murali.expensetracker.controller;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.model.LoginModel;
import com.murali.expensetracker.response.ResponseStatus;
import com.murali.expensetracker.service.JwtService;
import com.murali.expensetracker.service.UserLoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private JwtService jwtService;
    public AuthenticationController(JwtService jwtService){
        this.jwtService=jwtService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Handles authentication endpoint for user login
     * @param loginModel containing emailId and password
     * @return JWT for further requests that could be made by the user*/
    @PostMapping("/auth")
    public String authenticateUser(@RequestBody LoginModel loginModel){
        User user = jwtService.verifyUser(loginModel);
        return jwtService.generateToken(user.getUserId());
    }
}
