package com.github.yuriiyukh.stogram.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.yuriiyukh.stogram.payload.request.LoginRequest;
import com.github.yuriiyukh.stogram.payload.request.SignUpRequest;
import com.github.yuriiyukh.stogram.payload.response.JWTTokenSuccsessResponse;
import com.github.yuriiyukh.stogram.payload.response.MessageResponse;
import com.github.yuriiyukh.stogram.security.JWTTokenProvider;
import com.github.yuriiyukh.stogram.security.SecurityConstants;
import com.github.yuriiyukh.stogram.service.UserService;
import com.github.yuriiyukh.stogram.validation.ResponseErrorValidation;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    private final ResponseErrorValidation responseErrorValidation;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    public AuthController(ResponseErrorValidation responseErrorValidation, UserService userService,
            AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.responseErrorValidation = responseErrorValidation;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpRequest signUpRequest,
            BindingResult bindingResult) {

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        userService.createUser(signUpRequest);

        return ResponseEntity.ok(new MessageResponse("Registration succsessful"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authentificateUser(@Valid @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult) {

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTTokenSuccsessResponse(true, jwt));
    }
}
