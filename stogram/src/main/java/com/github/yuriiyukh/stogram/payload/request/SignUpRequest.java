package com.github.yuriiyukh.stogram.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.github.yuriiyukh.stogram.annotation.PasswordMatches;
import com.github.yuriiyukh.stogram.annotation.ValidEmail;

import lombok.Data;

@Data
@PasswordMatches
public class SignUpRequest {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email should not be empty")
    @ValidEmail
    private String email;

    @NotEmpty(message = "First name should not be empty")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    private String lastName;

    @NotEmpty(message = "User name should not be empty")
    private String userName;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 3)
    private String password;

    @NotEmpty(message = "Confirm password should not be empty")
    private String confirmPassword;
}
