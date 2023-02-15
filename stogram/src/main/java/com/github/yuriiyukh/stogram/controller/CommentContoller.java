package com.github.yuriiyukh.stogram.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.yuriiyukh.stogram.dto.CommentDTO;
import com.github.yuriiyukh.stogram.entity.Comment;
import com.github.yuriiyukh.stogram.facade.CommentFacade;
import com.github.yuriiyukh.stogram.payload.response.MessageResponse;
import com.github.yuriiyukh.stogram.service.CommentService;
import com.github.yuriiyukh.stogram.validation.ResponseErrorValidation;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentContoller {

    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final ResponseErrorValidation responseErrorValidation;

    public CommentContoller(CommentService commentService, CommentFacade commentFacade,
            ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
            @PathVariable("postId") String postId, BindingResult bindingResult, Principal principal) {

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO createdDTO = commentFacade.commentToCommentDTO(comment);

        return new ResponseEntity<>(createdDTO, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getPostComments(@PathVariable("postId") String postId) {
        List<CommentDTO> commentsDTO = commentService.findAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {

        commentService.deleteComment(Long.parseLong(commentId));

        return new ResponseEntity<>(new MessageResponse("Comment have been deleted"), HttpStatus.OK);
    }

}
