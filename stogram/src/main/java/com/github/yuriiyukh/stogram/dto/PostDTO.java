package com.github.yuriiyukh.stogram.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class PostDTO {

    private Long id;
    private String title;
    private String caption;
    private String location;
    private String userName;
    private Integer likes;

    private Set<String> likedUsers = new HashSet<>();
}
