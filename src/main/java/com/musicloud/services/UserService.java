package com.musicloud.services;

import com.musicloud.models.User;
import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public UserService(UserRepository userRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    public EditProfileDto getEditProfileDto(String email) {
        return userRepository.findProfileDtoOf(email);
    }

    public void editProfile(EditProfileDto editProfileDto, AppUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).get();

        if (!editProfileDto.getUsername().trim().isEmpty())
            user.setUsername(editProfileDto.getUsername());
        if (!editProfileDto.getFirstName().trim().isEmpty())
            user.setFirstName(editProfileDto.getFirstName());
        if (!editProfileDto.getLastName().trim().isEmpty())
            user.setLastName(editProfileDto.getLastName());

        userRepository.save(user);

        userDetails.setDisplayName(user.getUsername());
    }
}
