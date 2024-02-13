package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.PasswordToken;
import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.entity.UserVerification;
import com.murali.expensetracker.event.UserRegistrationEvent;
import com.murali.expensetracker.exception.UserNotActivatedException;
import com.murali.expensetracker.model.PasswordModel;
import com.murali.expensetracker.repository.PasswordTokenRepository;
import com.murali.expensetracker.repository.UserRepository;
import com.murali.expensetracker.repository.UserVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetServiceImplementation implements PasswordResetService{
    @Autowired
    EmailService emailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordTokenRepository passwordTokenRepository;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    UserVerificationRepository userVerificationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Sends a password reset token email to the specified user's email address.
     *
     * @param email The email address of the user for whom the password reset token email is to be sent.
     * @param url   The base URL to be included in the password reset link.
     * @throws MailException               If there is an issue with sending the email.
     * @throws UserNotActivatedException   If the user account is not activated, and an activation link is sent instead.
     * @throws UsernameNotFoundException   If no user is found for the specified email address.
     */
    @Override
    public void resetPasswordTokenEmail(String email,String url) throws MailException, UserNotActivatedException {
        User user;
        Optional <User> userFromRepo = userRepository.getByEmailId(email);
        if(userFromRepo.isEmpty()){
           throw new UsernameNotFoundException("User not found for specified email id");
        }

        user = userFromRepo.get();
        if(!user.isEnabled()){
            Optional<UserVerification>userVerificationFromDB = userVerificationRepository.getByUser(user);
            if(userVerificationFromDB.isPresent()){
                UserVerification userVerification = userVerificationFromDB.get();
                userVerificationRepository.delete(userVerification);
                applicationEventPublisher.publishEvent(new UserRegistrationEvent(user,url));
            }
            else{
                applicationEventPublisher.publishEvent(new UserRegistrationEvent(user,url));
            }
            throw new UserNotActivatedException("Account not activated, check email for activation link !");
        }

        String token = UUID.randomUUID().toString();
        String toEmail = user.getEmailId();
        String subject = "Password reset";

        PasswordToken passwordToken = new PasswordToken(user,token);
        passwordTokenRepository.save(passwordToken);

        String mailBody = "Reset your password by clicking on the link below\n" + url + "/reset-password?token=" + token;

        emailService.sendEmail(toEmail, mailBody, subject);

    }

    /**
     * Validates the provided password reset token.
     * This method checks the validity of the given password reset token by querying the
     * passwordTokenRepository. If the token is found, it further checks if the token has
     * expired. If expired, the token is deleted, and "expired" is returned. If the token
     * is not found, "invalid" is returned. Otherwise, "valid" is returned.
     *
     * @param token The password reset token to be validated.
     * @return "valid" if the token is valid, "expired" if it has expired, and "invalid" if the token is not found.
     */
    @Override
    public String ValidResetToken(String token) {
        Optional<PasswordToken>optionalPasswordToken = passwordTokenRepository.getByResetToken(token);
        PasswordToken passwordToken;
        Calendar calendar = Calendar.getInstance();
        if(optionalPasswordToken.isEmpty()){
            return "invalid";
        }
        passwordToken = optionalPasswordToken.get();
        if((passwordToken.getTime().getTime() - calendar.getTime().getTime()) <= 0){
            passwordTokenRepository.delete(passwordToken);
            return "expired";
        }
        return "valid";
    }

    /**
     * Resets the password for the user associated with the provided email address.
     * This method retrieves the user by querying the userRepository using the email
     * address from the provided PasswordModel. If the user is found, the password is
     * updated with the encoded new password and saved back to the userRepository.
     * @param passwordModel The PasswordModel containing the email and the new password.
     * @throws UsernameNotFoundException If no user is found for the specified email.
     */
    @Override
    public void resetPassword(PasswordModel passwordModel) throws UsernameNotFoundException {
        Optional<User>optionalUser = userRepository.getByEmailId(passwordModel.getEmail());
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("enter valid email id !");
        }
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
        userRepository.save(user);
    }
    /**
     * Changes the password for the user associated with the provided email address.
     *
     * @param passwordModel The PasswordModel containing the email, current password, and the new password.
     * @return True if the password change is successful, false otherwise.
     * @throws UsernameNotFoundException If no user is found for the specified email.
     */
    @Override
    public boolean changePassword(PasswordModel passwordModel) {
        Optional<User>optionalUser = userRepository.getByEmailId(passwordModel.getEmail());
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("No user found for specified email");
        }
        User user = optionalUser.get();
        String currentPassword = user.getPassword();
        if(passwordEncoder.matches(passwordModel.getCurrentPassword(),currentPassword)){
            user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        else {
            return false;
        }
    }
}
