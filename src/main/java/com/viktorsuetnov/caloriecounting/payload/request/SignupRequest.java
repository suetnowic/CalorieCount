package com.viktorsuetnov.caloriecounting.payload.request;

import com.viktorsuetnov.caloriecounting.annotations.PasswordMatches;
import com.viktorsuetnov.caloriecounting.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @NotEmpty(message = "Please enter your username")
    private String username;
    @Email(message = "It should have email format")
    @NotBlank(message = "Email is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "password is required")
    @Size(min = 6)
    private String password;
    private String confirmPassword;
}
