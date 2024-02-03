package com.murali.expensetracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class UserVerification {
    private static final int EXPIRY_TIME = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Date time;
    @OneToOne
    @JoinColumn(name = "User_id",
            referencedColumnName="UserId",
            nullable = false)
    private User user;

    public UserVerification(User user, String token){
        this.user=user;
        this.token=token;
        this.time=calculationOfExpiryTime(EXPIRY_TIME);
    }

    private Date calculationOfExpiryTime(int expiryTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expiryTime);
        return new Date(calendar.getTime().getTime());
    }
}
