package com.musicloud.init;

import com.musicloud.models.Liked;
import com.musicloud.models.User;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, UserRoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("stili");
            user.setEmail("stili@i.c");
            user.setPassword(passwordEncoder.encode("r4peemee"));
            user.setImageUrl("https://res.cloudinary.com/dtzjbyjzq/image/upload/v1657215390/default-avatar_idvjto.png");
            user.setLiked(new Liked());
            user.addRole(roleRepository.findById(1L).get());
            user.addRole(roleRepository.findById(2L).get());

            userRepository.save(user);
        }

    }
}
