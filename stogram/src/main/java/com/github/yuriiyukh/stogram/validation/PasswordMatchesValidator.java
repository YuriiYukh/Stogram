package com.github.yuriiyukh.stogram.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.yuriiyukh.stogram.annotation.PasswordMatches;
import com.github.yuriiyukh.stogram.payload.request.SignUpRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {

        SignUpRequest userSignUpRequest = (SignUpRequest) obj;
        return userSignUpRequest.getPassword().equals(userSignUpRequest.getConfirmPassword());
    }

}
