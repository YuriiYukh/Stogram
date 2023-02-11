package com.github.yuriiyukh.stogram.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.yuriiyukh.stogram.dto.PostDTO;
import com.github.yuriiyukh.stogram.entity.Post;
import com.github.yuriiyukh.stogram.facade.PostFacade;
import com.github.yuriiyukh.stogram.payload.response.MessageResponse;
import com.github.yuriiyukh.stogram.service.PostService;
import com.github.yuriiyukh.stogram.validation.ResponseErrorValidation;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostFacade postFacade;
    private final PostService postService;
    private final ResponseErrorValidation responseErrorValidation;
    
    public PostController(PostFacade postFacade, PostService postService,
            ResponseErrorValidation responseErrorValidation) {
        this.postFacade = postFacade;
        this.postService = postService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult bindingResult,
            Principal principal) {
        
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        
        Post post = postService.createPost(postDTO, principal);
        PostDTO createdPost = postFacade.postToPostDTO(post);
        
        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        
        List<PostDTO> postsDTO = postService.findAll()
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }
    
    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getUserPosts(Principal principal) {
        
        List<PostDTO> postsDTO = postService.getUserPosts(principal)
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }
    
    @PostMapping("/{postId}/{userName}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId,
            @PathVariable("userName") String userName) {
        
        Post post = postService.likePost(Long.parseLong(postId), userName);
        PostDTO postDTO = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);

    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal) {

        postService.deletePost(Long.parseLong(postId), principal);

        return new ResponseEntity<>(new MessageResponse("Post have been deleted"), HttpStatus.OK);
    }
    
}
