package com.musicloud.services;

import com.musicloud.models.User;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userRepository.existsByEmail(username)) throw new UsernameNotFoundException("Username not found.");
        User user = userRepository.findByEmail(username);
        List<GrantedAuthority> authorities = user.getRoles()
                .stream().map(u -> "ROLE_" + u.getName().name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new AppUserDetails(user, authorities);
    }
}
