package com.github.yuriiyukh.stogram.payload.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LoginRequest {

    @NotEmpty(message = "Username cannot be empty")
    private String userName;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
    
}
