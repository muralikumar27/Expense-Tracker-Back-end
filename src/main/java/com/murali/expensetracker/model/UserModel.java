package com.murali.expensetracker.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private long id;
    @NotBlank
    @NotNull
    private String name;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String password;

}
