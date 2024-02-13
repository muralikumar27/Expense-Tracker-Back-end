package com.murali.expensetracker.controller;

import com.murali.expensetracker.model.PasswordModel;
import com.murali.expensetracker.response.ResponseStatus;
import com.murali.expensetracker.service.PasswordResetService;
import com.murali.expensetracker.service.UrlCreation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    /**
     * When a user click forgot password it will be directed to
     * this controller, where the base url will be extracted
     * by the create application Url method and
     * resetPasswordTokenEmail(passwordModel.getEmail(),applicationUrl)
     * method will be called.If there is no exceptions thrown in further
     * function calls response status as success will be returned.
     * @param passwordModel consists of user email
     * @param request http request
     * @return ResponseEntity<ResponseStatus> if the mail is sent successfully.
     * @throws Exception if there is any errors in future function calls.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseStatus>resetPasswordTokenEmail(@RequestBody PasswordModel passwordModel, HttpServletRequest request) throws Exception {
        String applicationUrl = urlCreation.createApplicationUrl(request);
        passwordResetService.resetPasswordTokenEmail(passwordModel.getEmail(),applicationUrl);
        ResponseStatus responseStatus = new ResponseStatus("Password reset mail sent !", HttpStatus.OK);
        return new ResponseEntity<>(responseStatus,HttpStatus.OK);
    }

    /**
     * Handles the POST request for resetting the user's password using a reset token.
     * This method validates the provided reset token using the ValidResetToken method
     * from the passwordResetService. Depending on the validation result, the password
     * is reset if the token is valid. Returns a ResponseEntity with the corresponding
     * ResponseStatus and HTTP status code.
     * @param passwordModel The PasswordModel containing the new password and email id.
     * @param token         The reset token for validating the password reset request.
     * @return ResponseEntity<ResponseStatus> with the result of the password reset operation.
     * @throws Exception If there is an issue during the password reset process.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseStatus>resetPassword(@RequestBody PasswordModel passwordModel,@RequestParam("token") String token) throws Exception {
        String validation = passwordResetService.ValidResetToken(token);
        ResponseStatus responseStatus;
        if(validation.equals("invalid")){
            responseStatus = new ResponseStatus(" Password reset token invalid !",HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(responseStatus,HttpStatus.UNAUTHORIZED);
        }
        if(validation.equals("expired")){
            responseStatus = new ResponseStatus(" Password reset token expired !",HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(responseStatus,HttpStatus.UNAUTHORIZED);
        }
        if (validation.equals("valid")){
            passwordResetService.resetPassword(passwordModel);
        }

        responseStatus = new ResponseStatus(" Password reset successful !",HttpStatus.OK);
        return new ResponseEntity<>(responseStatus,HttpStatus.OK);
    }
    /**
     * Handles the POST request for changing the user's password.
     * This method attempts to change the user's password using the changePassword
     * method from the passwordResetService. Returns a ResponseEntity with the
     * corresponding ResponseStatus and HTTP status code.
     * Note: This method might undergo modifications after the completion of the login module.
     * @param passwordModel The PasswordModel containing the current password, new password and email id.
     * @return ResponseEntity<ResponseStatus> with the result of the password change operation.
     */
    @PostMapping("/change-password")
    public ResponseEntity<ResponseStatus>changePassword(@Valid @RequestBody PasswordModel passwordModel){
        boolean changed = passwordResetService.changePassword(passwordModel);
        ResponseStatus responseStatus;
        if(changed){
            responseStatus = new ResponseStatus("Password changed successfully !", HttpStatus.OK);
        }
        else {
            responseStatus = new ResponseStatus("Enter correct current password", HttpStatus.OK);
        }
        return new ResponseEntity<>(responseStatus,HttpStatus.OK);
    }
}
