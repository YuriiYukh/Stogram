package com.github.yuriiyukh.stogram.facade;

import com.github.yuriiyukh.stogram.annotation.Facade;
import com.github.yuriiyukh.stogram.dto.CommentDTO;
import com.github.yuriiyukh.stogram.entity.Comment;

@Facade
public class CommentFacade {
    
    public CommentDTO commentToCommentDTO(Comment comment) {
        
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage(comment.getMessage());
        commentDTO.setUserName(comment.getUsername());
        
        return commentDTO;
    }
}
