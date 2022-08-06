package com.musicloud.web;

import com.musicloud.models.Liked;
import com.musicloud.models.User;
import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() throws Exception {
        addTestUser();
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
    @WithAnonymousUser
    void testGetLandingPageForAnonymousUser() throws Exception {
        mockMvc.perform(get("/")).andExpect(view().name("welcome"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetLandingPageForLoggedInUser() throws Exception {
        mockMvc.perform(get("/")).andExpect(view().name("index"));
    }

    @Test
    @WithAnonymousUser
    void testGetLibraryPageForAnonymousUser() throws Exception {
        mockMvc.perform(get("/library"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetLibraryPageForLoggedInUser() throws Exception {
        mockMvc.perform(get("/library")).andExpect(view().name("index"));
    }
}

//TODO: unittest dashboard
