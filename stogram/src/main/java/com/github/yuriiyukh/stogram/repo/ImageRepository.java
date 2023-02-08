package com.github.yuriiyukh.stogram.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.yuriiyukh.stogram.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByUserId(Long id);
    
    Optional<Image> findByPostId(Long id);
    
    
}
