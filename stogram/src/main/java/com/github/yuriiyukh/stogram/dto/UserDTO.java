package com.github.yuriiyukh.stogram.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
    private String userName;
    private String bio;
}
