package com.murali.expensetracker.service;

import com.murali.expensetracker.entity.PasswordToken;
import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.event.UserRegistrationEvent;
import com.murali.expensetracker.exception.UserNotActivatedException;
import com.murali.expensetracker.repository.PasswordTokenRepository;
import com.murali.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    UrlCreation urlCreation;

    @Override
    public void resetPasswordTokenEmail(String email,String url) throws MailException, UserNotActivatedException {
        User user;
        Optional <User> userFromRepo = userRepository.getByEmailId(email);
        if(userFromRepo.isEmpty()){
           throw new UsernameNotFoundException("User not found for specified email id");
        }

        user = userFromRepo.get();
        if(!user.isEnabled()){
            throw new UserNotActivatedException("Account not activated !");
        }

        String token = UUID.randomUUID().toString();
        String toEmail = user.getEmailId();
        String subject = "Password reset";

        PasswordToken passwordToken = new PasswordToken(user,token);
        passwordTokenRepository.save(passwordToken);

        String mailBody = "Reset your password by clicking on the link below\n" + url + "/resetPassword?token=" + token;

        emailService.sendEmail(toEmail, mailBody, subject);

    }
}
