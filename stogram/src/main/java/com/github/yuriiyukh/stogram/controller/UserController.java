package com.github.yuriiyukh.stogram.controller;

import java.security.Principal;

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

import com.github.yuriiyukh.stogram.dto.UserDTO;
import com.github.yuriiyukh.stogram.entity.UserEntity;
import com.github.yuriiyukh.stogram.facade.UserFacade;
import com.github.yuriiyukh.stogram.service.UserService;
import com.github.yuriiyukh.stogram.validation.ResponseErrorValidation;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    
    private final UserService userService;
    private final UserFacade userFacade;
    private final ResponseErrorValidation responseErrorValidation;
    
    public UserController(UserService userService, UserFacade userFacade, ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
    }
    
    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        
        UserEntity user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);
        
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        
        UserEntity user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);
        
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    
    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal) {
        
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        
        UserEntity user = userService.updateUser(userDTO, principal);
        UserDTO updatedUser = userFacade.userToUserDTO(user);
        
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
