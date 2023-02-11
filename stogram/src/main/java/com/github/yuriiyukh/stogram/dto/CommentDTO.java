package com.github.yuriiyukh.stogram.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class CommentDTO {

    private Long id;
    
    @NotEmpty
    private String userName;
    
    @NotEmpty
    private String message;
    
}
