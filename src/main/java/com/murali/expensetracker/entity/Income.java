package com.murali.expensetracker.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Income {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long incomeId;

    @Column(nullable = false)
    @Positive
    private double incomeAmount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String Source;

    @ManyToOne
    @JoinColumn(name = "user_id",
    referencedColumnName = "userId",
    nullable = false)
    private User user;


}
