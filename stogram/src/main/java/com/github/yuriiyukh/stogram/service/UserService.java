package com.github.yuriiyukh.stogram.service;

import java.security.Principal;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.yuriiyukh.stogram.dto.UserDTO;
import com.github.yuriiyukh.stogram.entity.UserEntity;
import com.github.yuriiyukh.stogram.entity.enums.UserRoles;
import com.github.yuriiyukh.stogram.payload.exeption.UserExistException;
import com.github.yuriiyukh.stogram.payload.request.SignUpRequest;
import com.github.yuriiyukh.stogram.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public UserEntity createUser(SignUpRequest signUpRequest) {
        
        UserEntity user = new UserEntity();
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setUserName(signUpRequest.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
        user.getRoles().add(UserRoles.ROLE_USER);
        
        try {
            log.info("Saving user " + signUpRequest.getEmail());
            return userRepository.save(user);
        } catch(Exception ex) {
            log.error("Error during registration " + ex.getMessage());
            throw new UserExistException("The user " + user.getUsername() + "already exists");
        }
    }
    
    public UserEntity updateUser(UserDTO userDTO, Principal principal) {
        UserEntity user = getUserByPrincipal(principal);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setBio(userDTO.getBio());
        
        return userRepository.save(user);
    }
    
    public UserEntity getCurrentUser(Principal principal) {
        
        return getUserByPrincipal(principal);
    }
    
    private UserEntity getUserByPrincipal(Principal principal) {
        
        String userName = principal.getName();
        
        return userRepository.findUserByUserName(userName).
                orElseThrow(() -> new UsernameNotFoundException("User " + userName + " not found"));
    }
}
