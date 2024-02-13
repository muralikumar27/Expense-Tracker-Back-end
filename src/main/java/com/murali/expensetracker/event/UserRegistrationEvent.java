package com.murali.expensetracker.event;

import com.murali.expensetracker.entity.User;
import lombok.*;
import org.springframework.context.ApplicationEvent;

/**
* This class is used to create the event for user registration verification process.
* In this class event is created with the arguments got from the publishEvent()
*/
@Getter
@Setter
public class UserRegistrationEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;
    public UserRegistrationEvent(User user, String applicationUrl ) {
        super(user);
        this.user=user;
        this.applicationUrl=applicationUrl;
    }
}
