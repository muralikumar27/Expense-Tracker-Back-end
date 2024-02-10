package com.murali.expensetracker.controller;

import com.murali.expensetracker.model.PasswordModel;
import com.murali.expensetracker.response.ResponseStatus;
import com.murali.expensetracker.service.PasswordResetService;
import com.murali.expensetracker.service.UrlCreation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;
    @Autowired
    private UrlCreation urlCreation;
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseStatus>resetPasswordTokenEmail(@RequestBody PasswordModel passwordModel, HttpServletRequest request) throws Exception {
        String applicationUrl = urlCreation.createApplicationUrl(request);
        passwordResetService.resetPasswordTokenEmail(passwordModel.getEmail(),applicationUrl);
        ResponseStatus responseStatus = new ResponseStatus("Password reset mail sent !", HttpStatus.OK);
        return new ResponseEntity<>(responseStatus,HttpStatus.OK);
    }
}
