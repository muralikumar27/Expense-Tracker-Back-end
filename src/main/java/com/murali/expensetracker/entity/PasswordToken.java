package com.murali.expensetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordToken {
    private static final int EXPIRY_TIME = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String resetToken;
    @Column(nullable = false)
    private Date time;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    public PasswordToken(User user, String token){
        this.user=user;
        this.resetToken=token;
        this.time=calculationOfExpiryTime(EXPIRY_TIME);
    }

    private Date calculationOfExpiryTime(int expiryTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expiryTime);
        return new Date(calendar.getTime().getTime());
    }

}
