package com.github.yuriiyukh.stogram.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.yuriiyukh.stogram.entity.Image;
import com.github.yuriiyukh.stogram.entity.Post;
import com.github.yuriiyukh.stogram.entity.UserEntity;
import com.github.yuriiyukh.stogram.payload.exeption.ImageNotFoundException;
import com.github.yuriiyukh.stogram.payload.exeption.PostNotFoundException;
import com.github.yuriiyukh.stogram.repo.ImageRepository;
import com.github.yuriiyukh.stogram.repo.PostRepository;
import com.github.yuriiyukh.stogram.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    
    public ImageService(ImageRepository imageRepository, UserRepository userRepository, PostRepository postRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Image uploadImageForUser(MultipartFile file, Principal principal) throws IOException {
        
        UserEntity user = getUserByPrincipal(principal);
        
        log.info("Uploading image for user " + user.getUsername());
        
        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }
        Image image = new Image();
        image.setUserId(user.getId());
        image.setImageBytes(compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());
        
        return imageRepository.save(image);
    }
    
    public Image uploadImageForPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        
        UserEntity user = getUserByPrincipal(principal);
        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toSinglePostCollector());
        
        Image image = new Image();
        image.setPostId(postId);
        image.setImageBytes(compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());

        log.info("Uploading image for post " + post.getTitle());
        
        return imageRepository.save(image);
    }
    
    public Image getImageToUser(Principal principal) {
        
        UserEntity user = getUserByPrincipal(principal);
        
        Image image = imageRepository.findByUserId(user.getId()).orElse(null);
        
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(decompressBytes(image.getImageBytes()));
        }
        
        return image;
    }
    
    public Image getImageToPost(Long postId) {

        Image image = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("No image given for post with id " + postId));

        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(decompressBytes(image.getImageBytes()));
        }

        return image;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    private UserEntity getUserByPrincipal(Principal principal) {

        String userName = principal.getName();

        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User " + userName + " not found"));
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            if (list.size() != 1) {
                throw new IllegalArgumentException();
            }

            return list.get(0);
        });
    }
}
