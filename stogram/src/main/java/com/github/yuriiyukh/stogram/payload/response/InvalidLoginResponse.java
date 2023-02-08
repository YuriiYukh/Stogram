package com.github.yuriiyukh.stogram.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {

    private String userName;
    private String password;
    
    public InvalidLoginResponse() {
        this.userName = "Invalid username";
        this.password = "Invalid password";
    }
    
}
