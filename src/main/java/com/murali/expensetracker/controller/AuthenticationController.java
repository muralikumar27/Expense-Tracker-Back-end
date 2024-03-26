package com.murali.expensetracker.controller;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.model.LoginModel;
import com.murali.expensetracker.response.AuthResponse;
import com.murali.expensetracker.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Handles authentication endpoint for user login
     * @param loginModel containing emailId and password
     * @return JWT for further requests that could be made by the user*/
    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginModel loginModel){
        User user;
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginModel.getEmail(),loginModel.getPassword()));
        if(authentication.isAuthenticated()){
            user = jwtService.getUser(loginModel.getEmail());
        }
        else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
        String jwt = jwtService.generateToken(user.getUserId());
        AuthResponse authResponse = new AuthResponse(jwt, HttpStatus.OK);
        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }

}
