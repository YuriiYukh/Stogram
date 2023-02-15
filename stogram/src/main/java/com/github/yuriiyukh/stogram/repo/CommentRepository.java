package com.github.yuriiyukh.stogram.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.yuriiyukh.stogram.entity.Comment;
import com.github.yuriiyukh.stogram.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    Comment findByIdAndUserId(Long id, Long userId);

}
