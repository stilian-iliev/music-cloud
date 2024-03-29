package com.musicloud.services;

import com.musicloud.models.Liked;
import com.musicloud.models.ResetPasswordRequest;
import com.musicloud.models.User;
import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.models.enums.UserRoleEnum;
import com.musicloud.models.exceptions.UserNotFoundException;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.ResetPasswordRequestRepository;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final UserRoleRepository roleRepository;
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;

    private final UserDetailsService userDetailsService;
    private final SongService songService;

    public AuthService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, ModelMapper mapper, UserRoleRepository roleRepository, ResetPasswordRequestRepository resetPasswordRequestRepository, UserDetailsService userDetailsService, SongService songService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.resetPasswordRequestRepository = resetPasswordRequestRepository;
        this.userDetailsService = userDetailsService;
        this.songService = songService;
    }


    public void register(RegisterDto registerDto) {
        User user = mapper.map(registerDto, User.class);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setLiked(new Liked());
        user.addRole(roleRepository.findByName(UserRoleEnum.USER));
        //set up default user photo
        user.setImageUrl("https://res.cloudinary.com/dtzjbyjzq/image/upload/v1657215390/default-avatar_idvjto.png");

        userRepository.save(user);

        emailService.sendEmail(user.getEmail(), "Welcome!", "Thanks for joining us!");
        login(user.getEmail());
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
        changePassword(userDetails.getId(), newPassword);
    }

    public void changePassword(UUID uuid, String newPassword) {
        User user = userRepository.findById(uuid).orElseThrow(UserNotFoundException::new);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.getSongs().forEach(s -> songService.deleteSong(s.getId(), user.getId()));
        user.getFollowedUsers().forEach(user::unfollowUser);
        user.getFollowers().forEach(f-> f.unfollowUser(user));
        resetPasswordRequestRepository.deleteAllByUser(user);
        userRepository.delete(user);
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

    public ResetPasswordRequest generateResetPasswordRequest(String email) {
        User user = userRepository.findByEmail(email);
        ResetPasswordRequest rpr = new ResetPasswordRequest();
        rpr.setUser(user);
        return resetPasswordRequestRepository.save(rpr);
    }

    public void changePassword(ResetPasswordRequest request, String password) {
        changePassword(request.getUser().getId(), password);
        request.setUsed(true);
        resetPasswordRequestRepository.save(request);
    }

    public void login(String userName) {
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(userName);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.
                getContext().
                setAuthentication(auth);
    }
}
