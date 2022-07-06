package com.musicloud.services;

import com.musicloud.models.User;
import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private StorageService storageService;

    public UserService(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public EditProfileDto getEditProfileDto(String email) {
        return userRepository.findProfileDtoOf(email);
    }

    public void editProfile(EditProfileDto editProfileDto, AppUserDetails userDetails) throws IOException {
        User user = userRepository.findById(userDetails.getId()).orElseThrow();

        if (!editProfileDto.getUsername().trim().isEmpty()) {
            user.setUsername(editProfileDto.getUsername());
        }

        user.setFirstName(editProfileDto.getFirstName());
        user.setLastName(editProfileDto.getLastName());
        if (!editProfileDto.getImage().isEmpty()) {
            String imageUrl = storageService.saveImage(editProfileDto.getImage());
            user.setImageUrl(imageUrl);
        }
        userRepository.save(user);

        userDetails.setDisplayName(user.getUsername());
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}
