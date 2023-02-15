package com.github.yuriiyukh.stogram.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.yuriiyukh.stogram.dto.PostDTO;
import com.github.yuriiyukh.stogram.entity.Image;
import com.github.yuriiyukh.stogram.entity.Post;
import com.github.yuriiyukh.stogram.entity.UserEntity;
import com.github.yuriiyukh.stogram.payload.exeption.PostNotFoundException;
import com.github.yuriiyukh.stogram.repo.ImageRepository;
import com.github.yuriiyukh.stogram.repo.PostRepository;
import com.github.yuriiyukh.stogram.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {

        UserEntity user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        log.info("Saving new post to db for User {}", user.getEmail());

        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getByPostId(Long postId, Principal principal) {

        UserEntity user = getUserByPrincipal(principal);

        return postRepository.findPostByIdAndUserId(postId, user.getId()).orElseThrow(() -> new PostNotFoundException(
                "Post with id " + postId + " and user id " + user.getId() + " does not exist."));
    }

    public List<Post> getUserPosts(Principal principal) {

        UserEntity user = getUserByPrincipal(principal);

        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long postId, String userName) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found witn id " + postId));

        Optional<String> userLiked = post.getLikedUsers().stream().filter(user -> user.equals(userName)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(userName);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(userName);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {

        Post post = getByPostId(postId, principal);
        Optional<Image> image = imageRepository.findByPostId(postId);
        postRepository.delete(post);

        if (image.isPresent()) {
            imageRepository.delete(image.get());
        }
    }

    private UserEntity getUserByPrincipal(Principal principal) {

        String userName = principal.getName();

        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User " + userName + " not found"));
    }
}
