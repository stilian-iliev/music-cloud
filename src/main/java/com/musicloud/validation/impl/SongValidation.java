package com.musicloud.validation.impl;

import com.musicloud.services.StorageService;
import com.musicloud.validation.ValidSong;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SongValidation implements ConstraintValidator<ValidSong, MultipartFile> {
    @Override
    public void initialize(ValidSong constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return !value.isEmpty() && value.getSize() < StorageService.MAX_SONG_SIZE;
    }

}
