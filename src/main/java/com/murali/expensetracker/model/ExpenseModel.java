package com.murali.expensetracker.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseModel {
    @NotBlank
    private double amount;
    @NotBlank
    private LocalDate date;
    private String description;
    @NotBlank
    private String expenseCategory;
}
