package com.musicloud.services;

import com.musicloud.models.User;
import com.musicloud.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userRepository.existsByEmail(username)) throw new UsernameNotFoundException("Username not found.");
        User user = userRepository.findByEmail(username);

        String[] roles = user.getRoles()
                .stream().map(u -> "ROLE_" + u.getName().name())
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(roles)
                .build();
    }
}
