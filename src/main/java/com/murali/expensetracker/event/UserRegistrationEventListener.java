package com.murali.expensetracker.event;

import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.service.EmailService;
import com.murali.expensetracker.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import java.util.UUID;
/*
* Once the UserRegistrationEvent is published
* this class will listen to the event and the
* userRegistrationEvent class contains the source of the event,
* that is the user who invoked to event and the base Url.
* onApplicationEvent from ApplicationListener interface is
* implemented to generate verification token and invoke
* sendVerificationEmail(toEmail, mailBody, subject) method
* from EmailService class and before sending mail to the user
* the verification token will be stored in database with
* corresponding user id.
*/
@Component
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {

    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    private EmailService emailService;
    @Override
    public void onApplicationEvent(UserRegistrationEvent event)throws MailException {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        String toEmail = user.getEmailId();
        String subject = "Account verification";
        userRegistrationService.saveUserToken(user, token);

        String mailBody = "Please verify your account by clicking on the link below\n" + event.getApplicationUrl() + "/verify-user?token=" + token;

        emailService.sendVerificationEmail(toEmail, mailBody, subject);

    }
}
