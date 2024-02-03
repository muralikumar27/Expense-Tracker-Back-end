package com.murali.expensetracker.entity;
import jakarta.validation.constraints.Email;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "user_name",
    nullable = false)
    private String name;

    @Column(unique = true,
    nullable = false)
    @Email
    private String emailId;

    @Column(length = 60,
    nullable = false)
    private String password;

    private boolean enabled = false;

    @Column(nullable = false)
    private String role;
}
