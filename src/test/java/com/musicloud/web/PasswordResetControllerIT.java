package com.musicloud.web;

import com.musicloud.models.Liked;
import com.musicloud.models.ResetPasswordRequest;
import com.musicloud.models.User;
import com.musicloud.repositories.*;
import com.musicloud.services.AuthService;
import com.musicloud.services.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordResetControllerIT {
    @MockBean
    private EmailService mockEmailService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResetPasswordRequestRepository resetPasswordRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        addTestUser();

    }

    @AfterEach
    void tearDown() {
        resetPasswordRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    void addTestUser() {
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());

        testUser = userRepository.save(user);
    }

    @Test
    void testGettingSendingPasswordRequestPage() throws Exception {
        mockMvc.perform(get("/request-reset-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("request-reset-password"));
    }

    @Test
    void testSendingPasswordRequest() throws Exception {
        mockMvc.perform(post("/request-reset-password")
                .with(csrf())
                .param("email", testUser.getEmail()))
                .andExpect(status().isOk())
                .andExpect(view().name("request-reset-password-post"));

        verify(mockEmailService).sendResetPasswordEmail(testUser.getEmail(), resetPasswordRequestRepository.findByUserId(testUser.getId()).getId());
    }

    @Test
    void testSendingPasswordRequestWithInvalidEmail() throws Exception {
        mockMvc.perform(post("/request-reset-password")
                        .with(csrf())
                        .param("email", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/request-reset-password"))
                .andExpect(model().attributeHasErrors());

    }

    @Test
    void testSendingPasswordRequestWithNotExistingEmail() throws Exception {
        mockMvc.perform(post("/request-reset-password")
                        .with(csrf())
                        .param("email", "invalid@user.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/request-reset-password"))
                .andExpect(model().attributeHasErrors());

    }

    @Test
    void testGetResetPasswordPage() throws Exception {
        authService.generateResetPasswordRequest(testUser.getEmail());

        mockMvc.perform(get("/reset-password/" + resetPasswordRequestRepository.findByUserId(testUser.getId()).getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password"))
                ;

    }

    @Test
    void testResetPassword() throws Exception {
        authService.generateResetPasswordRequest(testUser.getEmail());

        mockMvc.perform(post("/reset-password/" + resetPasswordRequestRepository.findByUserId(testUser.getId()).getId())
                        .with(csrf())
                        .param("password", "changedPass")
                        .param("confirmPassword", "changedPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
        ;

        Assertions.assertTrue(passwordEncoder.matches("changedPass", userRepository.findByEmail(testUser.getEmail()).getPassword()));

    }

    @Test
    void testResetPasswordWithShortPass() throws Exception {
        authService.generateResetPasswordRequest(testUser.getEmail());

        ResetPasswordRequest request = resetPasswordRequestRepository.findByUserId(testUser.getId());
        mockMvc.perform(post("/reset-password/" + request.getId())
                        .with(csrf())
                        .param("password", "short")
                        .param("confirmPassword", "short"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reset-password/" + request.getId()))
                .andExpect(model().attributeHasErrors())
        ;

        Assertions.assertTrue(passwordEncoder.matches("topsecret", userRepository.findByEmail(testUser.getEmail()).getPassword()));

    }

    @Test
    void testResetPasswordWithNotMatchingPasswords() throws Exception {
        authService.generateResetPasswordRequest(testUser.getEmail());

        ResetPasswordRequest request = resetPasswordRequestRepository.findByUserId(testUser.getId());
        mockMvc.perform(post("/reset-password/" + request.getId())
                        .with(csrf())
                        .param("password", "changedPass")
                        .param("confirmPassword", "differentPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reset-password/" + request.getId()))
                .andExpect(model().attributeHasErrors())
        ;

        Assertions.assertTrue(passwordEncoder.matches("topsecret", userRepository.findByEmail(testUser.getEmail()).getPassword()));

    }
}
