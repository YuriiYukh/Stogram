package com.github.yuriiyukh.stogram.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.yuriiyukh.stogram.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserByUserName(String userName);
    
    Optional<UserEntity> findUserByEmail(String email);
    
}
