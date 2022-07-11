package com.musicloud.services;

import com.musicloud.models.Playlist;
import com.musicloud.models.User;
import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public AuthService(UserRepository userRepository, PlaylistRepository playlistRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }


    public void register(RegisterDto registerDto) {
        User user = mapper.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setLiked(new Playlist("Liked songs."));
        //set up default user photo
        user.setImageUrl("https://res.cloudinary.com/dtzjbyjzq/image/upload/v1657215390/default-avatar_idvjto.png");

        userRepository.save(user);
    }

    public boolean passwordCorrect(String raw) {
        AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return passwordEncoder.matches(raw, userDetails.getPassword());
    }

    public void changeEmail(AppUserDetails userDetails, String email) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        user.setEmail(email);
        userRepository.save(user);
        userDetails.setEmail(email);
    }

    public void changePassword(AppUserDetails userDetails, String newPassword) {
        User user = userRepository.findByEmail(userDetails.getUsername());

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteAccount(AppUserDetails userDetails) {
        userRepository.deleteById(userDetails.getId());
    }

    public void unauthorize() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }
}
