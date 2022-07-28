package com.musicloud.services;

import com.musicloud.models.Liked;
import com.musicloud.models.User;
import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.models.enums.UserRoleEnum;
import com.musicloud.models.exceptions.UserNotFoundException;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final UserRoleRepository roleRepository;

    public AuthService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, ModelMapper mapper, UserRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
    }


    public void register(RegisterDto registerDto) {
        User user = mapper.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setLiked(new Liked());
        user.addRole(roleRepository.findByName(UserRoleEnum.USER));
        //set up default user photo
        user.setImageUrl("https://res.cloudinary.com/dtzjbyjzq/image/upload/v1657215390/default-avatar_idvjto.png");

        userRepository.save(user);

//      TODO: send email;
        emailService.sendEmail(user.getEmail(), "Welcome!", "Thanks for joining us!");
    }

    public boolean passwordCorrect(String raw) {
        AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return passwordEncoder.matches(raw, userDetails.getPassword());
    }

    public void changeEmail(AppUserDetails userDetails, String email) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotFoundException::new);
        user.setEmail(email);
        userRepository.save(user);
        userDetails.setEmail(email);
    }

    public void changePassword(AppUserDetails userDetails, String newPassword) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotFoundException::new);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteAccount(UUID userId) {
        userRepository.deleteById(userId);
    }

    public void unauthorize() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }

    public void addRole(UUID userId, UserRoleEnum role) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.addRole(roleRepository.findByName(role));
        userRepository.save(user);
    }

    public void removeRole(UUID userId, UserRoleEnum role) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.removeRole(roleRepository.findByName(role));
        userRepository.save(user);
    }
}
