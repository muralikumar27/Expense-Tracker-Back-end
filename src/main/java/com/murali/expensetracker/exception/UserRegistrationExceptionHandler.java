package com.murali.expensetracker.exception;


import com.murali.expensetracker.error.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@ResponseStatus
public class UserRegistrationExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> userRegistrationExceptionHandler(UserAlreadyExistsException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> userRegistrationExceptionHandler(MethodArgumentNotValidException e) {
        ErrorMessage errorMessage = new ErrorMessage("please enter valid details", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorMessage> userVerificationExceptionHandler(MailException e){
        ErrorMessage errorMessage = new ErrorMessage("Unexpected error, please click re-send", HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage>userNotFoundException(UsernameNotFoundException e){
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(),HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
    @ExceptionHandler(UserNotActivatedException.class)
    public ResponseEntity<ErrorMessage>userNotActivatedException(UserNotActivatedException e){
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> userRegistrationExceptionHandler(Exception e) {
        e.printStackTrace();
        ErrorMessage errorMessage = new ErrorMessage("Error, try again", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
