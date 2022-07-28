package com.musicloud.web;

import com.musicloud.models.Liked;
import com.musicloud.models.User;
import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import com.musicloud.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private EmailService mockEmailService;

    private RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        addUser();
        registerDto = new RegisterDto();
        registerDto.setUsername("test");
        registerDto.setPassword("topsecret");
        registerDto.setConfirmPassword("topsecret");

    }

    void addUser() {
        User user = new User();
        user.setId(UUID.fromString("5048a6d4-0e8a-11ed-861d-0242ac120002"));
        user.setUsername("admin");
        user.setEmail("admin@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("https://res.cloudinary.com/dtzjbyjzq/image/upload/v1657215390/default-avatar_idvjto.png");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());
        System.out.println(user.getId());
        userRepository.save(user);
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
    @WithUserDetails("admin@test.com")
    void testSettingsPage() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(view().name("settings"));
    }

//    @Test
//    @WithUserDetails
//    void testChangingEmail() throws Exception {
//        mockMvc.perform(post("/settings/email")
//                        .param("email", "changed@test.com")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection());
//    }

//    @Test
//    @WithUserDetails("admin@musicloud.com")
//    void testChangingPassword() throws Exception {
//        mockMvc.perform(post("/settings/password")
//                        .param("oldPassword", "topsecret")
//                        .param("password", "changedpass")
//                        .param("confirmPassword", "changedpass")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection());
//    }
}
