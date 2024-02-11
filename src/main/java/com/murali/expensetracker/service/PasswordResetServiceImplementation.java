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
            UserVerification userVerification = userVerificationFromDB.get();
            userVerificationRepository.delete(userVerification);
            applicationEventPublisher.publishEvent(new UserRegistrationEvent(user,url));
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
            return "expired";
        }
        return "valid";
    }

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
}
