package com.musicloud.services;

import com.musicloud.models.User;
import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, StorageService storageService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
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

    public void changeEmail(AppUserDetails userDetails, String email) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        user.setEmail(email);
        userRepository.save(user);
        userDetails.setEmail(email);
    }

    public void changePassword(AppUserDetails userDetails, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (true) {
            //todo check if old password is correct
            return;
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void deleteAccount(AppUserDetails userDetails) {
        //todo
    }
}
