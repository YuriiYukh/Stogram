package com.github.yuriiyukh.stogram.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.yuriiyukh.stogram.entity.Image;
import com.github.yuriiyukh.stogram.payload.response.MessageResponse;
import com.github.yuriiyukh.stogram.service.ImageService;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadProfileImage(@RequestParam("file") MultipartFile file,
            Principal principal) throws IOException {

        imageService.uploadImageForUser(file, principal);

        return new ResponseEntity<>(new MessageResponse("Image uploaded successfully"), HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadPostImage(@PathVariable("postId") String postId,
            @RequestParam("file") MultipartFile file, Principal principal) throws IOException {

        imageService.uploadImageForPost(file, principal, Long.parseLong(postId));

        return new ResponseEntity<>(new MessageResponse("Image uploaded successfully"), HttpStatus.OK);
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Image> getProfileImage(Principal principal) {

        Image userImage = imageService.getImageToUser(principal);

        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<Image> getPostImage(@PathVariable("postId") String postId, Principal principal) {

        Image postImage = imageService.getImageToPost(Long.parseLong(postId));

        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }
}
