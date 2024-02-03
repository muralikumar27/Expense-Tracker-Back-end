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
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long budgetId;

    @Column(nullable = false)
    private double budgetAmount;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id",
    referencedColumnName = "userId",
    nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id",
    referencedColumnName = "CategoryId",
    nullable = false)
    private ExpenseCategory expenseCategory;


}
