package com.musicloud.validation.impl;

import com.musicloud.repositories.UserRepository;
import com.musicloud.validation.ExistingEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistingEmailValidation implements ConstraintValidator<ExistingEmail, String> {
    private final UserRepository userRepository;

    public ExistingEmailValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(ExistingEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userRepository.existsByEmail(value);
    }
}
