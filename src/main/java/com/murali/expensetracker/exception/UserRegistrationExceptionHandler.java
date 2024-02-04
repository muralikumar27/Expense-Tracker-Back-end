package com.murali.expensetracker.exception;


import com.murali.expensetracker.error.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        ErrorMessage errorMessage = new ErrorMessage("please enter valid details", HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> userRegistrationExceptionHandler() {
        ErrorMessage errorMessage = new ErrorMessage("Error while registering, try again", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}