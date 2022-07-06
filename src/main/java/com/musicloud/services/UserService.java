package com.musicloud.services;

import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.repositories.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public EditProfileDto getEditProfileDto() {
        return userRepository.findProfileDtoOf("stili@i.c");
    }
}
