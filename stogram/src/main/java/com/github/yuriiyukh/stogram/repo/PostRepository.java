package com.github.yuriiyukh.stogram.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.yuriiyukh.stogram.entity.Post;
import com.github.yuriiyukh.stogram.entity.UserEntity;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOrderByCreatedDateDesc(UserEntity user);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndUserId(Long id, Long userId);

}
