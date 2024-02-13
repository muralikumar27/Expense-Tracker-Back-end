package com.murali.expensetracker.service;


import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.entity.UserVerification;
import com.murali.expensetracker.exception.UserAlreadyExistsException;
import com.murali.expensetracker.model.UserModel;
import com.murali.expensetracker.repository.UserRepository;
import com.murali.expensetracker.repository.UserVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class UserRegistrationServiceImplementation implements UserRegistrationService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserVerificationRepository userVerificationRepository;
    @Autowired
    EmailService emailService;

    /**
     * Handles User registration functionality.
     *
     * @param userModel consists of email, name, password
     * @return User object after saving the user to the database
     * @throws Exception if user already exists with same email id.
     */
    @Override
    public User registerUser(UserModel userModel) throws Exception{
        User user = new User();
        user.setEmailId(userModel.getEmail());
        user.setName(userModel.getName());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setRole("user");
        if(userRepository.existsByEmailId(userModel.getEmail())){
            throw new UserAlreadyExistsException(userModel.getEmail()+" is already in use.");
        }
        return userRepository.save(user);

    }

    @Override
    public void saveUserToken(User user, String token) {
        UserVerification userVerification = new UserVerification(user,token);
        userVerificationRepository.save(userVerification);
    }

    /**
     * Handles validation of verification token.
    *This method will try to get UserVerification object
    * with the help of userVerificationRepository.getByToken(token)
    * if there exists a data in the userVerification table
    * with the token then the data from the database
    * will be stored as userVerification object, if there is no
    * data for the token "invalid" will be returned, if the token
    * timing is expired then "expired" will be returned, if the token is valid
    * user account will be enabled and "valid" will be returned.
     * @param token verification token entered by user in url
     * @return invalid if there exists no data for the token, expired if the token
     * is expired else user will be enabled and returns valid.
     *
    */
    @Override
    public String verifyUser(String token) {
        UserVerification userVerification = userVerificationRepository.getByToken(token);
        Calendar calendar = Calendar.getInstance();
        if(userVerification==null){
            return "invalid";
        }
        if((userVerification.getTime().getTime()-calendar.getTime().getTime()) <= 0){

            return "expired";
        }
        User user = userVerification.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        userVerificationRepository.delete(userVerification);
        return "valid";
    }

    /**
     * Handles verification token resend function.
     * If current token exists in DB then a new mail with new token
     * will be sent and old token will be deleted and new token will
     * be added to database for future validation.
     * @param token current token passed in url by user
     * @param applicationUrl application's base url
     * @return invalid if the current token provided by user is invalid, else valid.
     * @throws MailException if there is error in sending mail.
     */
    @Override
    public String resendVerificationToken(String token, String applicationUrl)throws MailException {
        UserVerification userVerification = userVerificationRepository.getByToken(token);
        if(userVerification==null){
            return "invalid";
        }
        User user = userVerification.getUser();
        userVerificationRepository.delete(userVerification);
        resendVerificationEmail(user,applicationUrl);
        return "valid";
    }

    /**
     * Resends the verification email to the specified user with a new verification token.
     * This method generates a new verification token, associates it with the user in the database,
     * and sends an email to the user for account verification.
     * @param user The User object for whom the verification email is to be resent.
     * @param applicationUrl The base URL of the application.
     * @throws MailException If there is an issue with sending the email.
     */
    private void resendVerificationEmail(User user , String applicationUrl) throws MailException {
        String newToken = UUID.randomUUID().toString();
        String toEmail = user.getEmailId();
        String subject = "Verify Account !";
        UserVerification userVerification = new UserVerification(user,newToken);
        userVerificationRepository.save(userVerification);
        String mailBody = "Please verify your account by clicking on the link below\n" + applicationUrl + "/verify-user/?token=" + newToken;

        emailService.sendEmail(toEmail, mailBody, subject);
    }
}
