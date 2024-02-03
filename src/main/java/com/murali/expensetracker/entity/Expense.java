package com.murali.expensetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long expenseId;
    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 500)
    private String description;

    @ManyToOne()
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId",
            nullable = false
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "CategoryId",
            nullable = false
    )
    private ExpenseCategory expenseCategory;

}
