package com.musicloud.services;

import com.musicloud.models.User;
import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.models.dtos.SongDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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
            userDetails.setDisplayName(editProfileDto.getUsername());
        }

        user.setFirstName(editProfileDto.getFirstName());
        user.setLastName(editProfileDto.getLastName());
        if (!editProfileDto.getImage().isEmpty()) {
            String imageUrl = storageService.saveImage(editProfileDto.getImage());
            if (imageUrl != null) {
                user.setImageUrl(imageUrl);
                userDetails.setImageUrl(imageUrl);
            }
        }
        userRepository.save(user);

    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public List<SongDto> getUserSongDtos(AppUserDetails appUserDetails) {
//        List<SongDto> songsFromUserWithId = userRepository.getSongsFromUserWithId(appUserDetails.getUsername());

        return null;
    }
}
