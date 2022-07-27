package com.musicloud.web;

import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.repositories.UserRepository;
import com.musicloud.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @MockBean
    private EmailService mockEmailService;

    private RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDto();
        registerDto.setUsername("test");
        registerDto.setPassword("topsecret");
        registerDto.setConfirmPassword("topsecret");

    }

    @Test
    void testRegistrationPageShown() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testUserRegistration() throws Exception {
        registerDto.setEmail("register@test.com");

        mockMvc.perform(post("/register")
                        .param("email", registerDto.getEmail())
                        .param("username", registerDto.getUsername())
                        .param("password", registerDto.getPassword())
                        .param("confirmPassword", registerDto.getConfirmPassword())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(mockEmailService).sendEmail(registerDto.getEmail(), "Welcome!", "Thanks for joining us!");

    }

    @Test
    void testUserRegisterWithBadCredentials() throws Exception {
        registerDto.setEmail("invalid-email");

        mockMvc.perform(post("/register")
                        .param("email", registerDto.getEmail())
                        .param("username", registerDto.getUsername())
                        .param("password", registerDto.getPassword())
                        .param("confirmPassword", registerDto.getConfirmPassword())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));

    }

    @Test
    void loginPageShown() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testUserLogin() throws Exception {
        registerDto.setEmail("login@test.com");

        mockMvc.perform(post("/register")
                        .param("email", registerDto.getEmail())
                        .param("username", registerDto.getUsername())
                        .param("password", registerDto.getPassword())
                        .param("confirmPassword", registerDto.getConfirmPassword())
                        .with(csrf()));

        mockMvc.perform(post("/login")
                        .param("username", registerDto.getEmail())
                        .param("password", registerDto.getPassword())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testUserLoginWithBadCredentials() throws Exception {
        registerDto.setEmail("invalid@test.com");

        mockMvc.perform(post("/login")
                        .param("username", registerDto.getEmail())
                        .param("password", registerDto.getPassword())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/login-error"));
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void testSettingsPage() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(view().name("settings"));
    }
}
