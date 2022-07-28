package com.musicloud.web;

import com.musicloud.models.Liked;
import com.musicloud.models.User;
import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.models.enums.UserRoleEnum;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import com.musicloud.services.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

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

    private AppUserDetails testUser;

    private RegisterDto registerDto;

    @BeforeEach
    void setUp() throws Exception {
        registerDto = new RegisterDto();
        registerDto.setUsername("test");
        registerDto.setPassword("topsecret");
        registerDto.setConfirmPassword("topsecret");
        addTestUser();

        User byEmail = userRepository.findByEmail("admin@test.com");
        this.testUser = new AppUserDetails(byEmail, byEmail.getRoles()
                .stream().map(u -> "ROLE_" + u.getName().name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    @AfterEach
    void tearDown() {
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

        Assertions.assertEquals(registerDto.getEmail(), userRepository.findByEmail(registerDto.getEmail()).getEmail());
        Assertions.assertEquals(registerDto.getUsername(), userRepository.findByEmail(registerDto.getEmail()).getUsername());
        Assertions.assertTrue(passwordEncoder.matches(registerDto.getPassword(), userRepository.findByEmail(registerDto.getEmail()).getPassword()));

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

        Assertions.assertEquals(1, userRepository.count());
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
    void testSettingsPage() throws Exception {

        mockMvc.perform(get("/settings")
                        .with(user(testUser))
                )
                .andExpect(view().name("settings"));
    }

    @Test
    void testChangingEmail() throws Exception {
        mockMvc.perform(post("/settings/email")
                        .param("email", "changed@test.com")
                        .with(csrf())
                        .with(user(testUser)))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals("changed@test.com", userRepository.findById(testUser.getId()).get().getEmail());
    }

    @Test
    void testChangingEmailWithInvalidEmail() throws Exception {
        mockMvc.perform(post("/settings/email")
                        .param("email", "invalid-email")
                        .with(csrf())
                        .with(user(testUser)))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(testUser.getUsername(), userRepository.findById(testUser.getId()).get().getEmail());
    }

    @Test
    void testChangingPassword() throws Exception {
        mockMvc.perform(post("/settings/password")
                        .param("oldPassword", "topsecret")
                        .param("password", "changedpass")
                        .param("confirmPassword", "changedpass")
                        .with(csrf())
                        .with(user(testUser))
                )
                .andExpect(status().is3xxRedirection());

        Assertions.assertTrue(passwordEncoder.matches("changedpass", userRepository.findById(testUser.getId()).get().getPassword()));
    }

    @Test
    void testChangingPasswordWithWrongPassword() throws Exception {
        mockMvc.perform(post("/settings/password")
                        .param("oldPassword", "incorrect")
                        .param("password", "changedpass")
                        .param("confirmPassword", "changedpass")
                        .with(csrf())
                        .with(user(testUser))
                )
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(testUser.getPassword(), userRepository.findByEmail(testUser.getUsername()).getPassword());
    }

    @Test
    void testDeletingAccount() throws Exception {
        mockMvc.perform(post("/settings/delete")
                        .with(csrf())
                        .with(user(testUser))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Assertions.assertFalse(userRepository.existsByEmail(testUser.getUsername()));
    }
}
