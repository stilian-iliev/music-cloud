package com.musicloud.services;

import com.musicloud.models.Playlist;
import com.musicloud.models.User;
import com.musicloud.models.dtos.RegisterDto;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.UserRepository;
import org.modelmapper.ModelMapper;
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
        user.setLiked(new Playlist(false, user.getUsername() + "'s liked songs."));

        userRepository.save(user);
    }
}
