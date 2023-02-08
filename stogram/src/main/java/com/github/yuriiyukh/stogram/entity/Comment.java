package com.github.yuriiyukh.stogram.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(columnDefinition = "text", nullable = false)
    private String message;
    
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate()
    {
        this.createdDate = LocalDateTime.now();
    }
}
