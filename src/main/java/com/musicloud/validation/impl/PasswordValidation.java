package com.musicloud.validation.impl;

import com.musicloud.services.AuthService;
import com.musicloud.validation.CorrectPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<CorrectPassword, String> {
    private final AuthService authService;

    public PasswordValidation(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void initialize(CorrectPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return authService.passwordCorrect(value);
    }
}
