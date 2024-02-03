package com.murali.expensetracker.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CategoryId;

    @Column(nullable = false)
    private String CategoryName;

    @ManyToOne
    @JoinColumn(name = "user_id",
    referencedColumnName = "userId",
    nullable = true)
    private User user;

}
