package com.github.yuriiyukh.stogram.facade;

import com.github.yuriiyukh.stogram.annotation.Facade;
import com.github.yuriiyukh.stogram.dto.PostDTO;
import com.github.yuriiyukh.stogram.entity.Post;

@Facade
public class PostFacade {

    public PostDTO postToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setUserName(post.getUser().getUsername());
        postDTO.setId(post.getId());
        postDTO.setCaption(post.getCaption());
        postDTO.setLikes(post.getLikes());
        postDTO.setLikedUsers(post.getLikedUsers());
        postDTO.setLocation(post.getLocation());
        postDTO.setTitle(post.getTitle());

        return postDTO;
    }
}
