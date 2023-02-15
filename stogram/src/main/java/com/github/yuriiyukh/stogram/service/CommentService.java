package com.github.yuriiyukh.stogram.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.yuriiyukh.stogram.dto.CommentDTO;
import com.github.yuriiyukh.stogram.entity.Comment;
import com.github.yuriiyukh.stogram.entity.Post;
import com.github.yuriiyukh.stogram.entity.UserEntity;
import com.github.yuriiyukh.stogram.payload.exeption.PostNotFoundException;
import com.github.yuriiyukh.stogram.repo.CommentRepository;
import com.github.yuriiyukh.stogram.repo.PostRepository;
import com.github.yuriiyukh.stogram.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {

        UserEntity user = getUserByPrincipal(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post with id " + postId));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setMessage(commentDTO.getMessage());
        comment.setUsername(user.getUsername());

        log.info("Add comment from user {} for post with id {}", user.getUsername(), post.getId());

        return commentRepository.save(comment);
    }

    public List<Comment> findAllCommentsForPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post with id " + postId + " found"));

        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
        }
    }

    private UserEntity getUserByPrincipal(Principal principal) {

        String userName = principal.getName();

        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User " + userName + " not found"));
    }
}
