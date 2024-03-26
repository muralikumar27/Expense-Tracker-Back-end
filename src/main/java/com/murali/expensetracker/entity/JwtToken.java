package com.murali.expensetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;

    private String token;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id",
    referencedColumnName = "UserId")
    private User user;

    public JwtToken(String jwt, boolean b, User user) {
        this.token = jwt;
        this.revoked = b;
        this.user = user;
    }
}
