package com.musicloud.init;

import com.musicloud.models.Playlist;
import com.musicloud.models.User;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, PlaylistRepository playlistRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            Playlist playlist = new Playlist();
            playlist.setName("playlist");

            User user = new User();
            user.setUsername("stili");
            user.setEmail("stili@i.c");
            user.setLiked(playlist);
            user.setPassword(passwordEncoder.encode("r4peemee"));

            playlistRepository.save(playlist);
            userRepository.save(user);
        }
    }
}
