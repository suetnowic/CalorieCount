package com.viktorsuetnov.caloriecounting.payload.request;

import com.viktorsuetnov.caloriecounting.annotations.PasswordMatches;
import com.viktorsuetnov.caloriecounting.annotations.ValidEmail;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@Data
@PasswordMatches
public class SignupRequest {

    @NotEmpty(message = "Please enter your username")
    @Size(min = 5, max = 100)
    private String username;

    @Email(message = "It should have email format")
    @NotBlank(message = "Email is required")
    @Size(max = 100)
    @ValidEmail
    private String email;

    @NotEmpty(message = "password is required")
    @Size(min = 8, max = 100)
    private String password;
    private String confirmPassword;
    @Range(min = 10, max = 10000)
    private Integer caloriesPerDay;
}
