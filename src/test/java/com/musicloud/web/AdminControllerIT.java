package com.musicloud.web;

import com.musicloud.models.Liked;
import com.musicloud.models.User;
import com.musicloud.models.UserRole;
import com.musicloud.models.enums.UserRoleEnum;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
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
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getAdminDashboardPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(view().name("admin_dashboard"));
    }

    @Test
    @WithAnonymousUser
    void getAdminDashboardPageWithAnon() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getAdminUsersPage() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(view().name("admin_users"));
    }

    @Test
    @WithAnonymousUser
    void getAdminUsersPageWithAnon() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddingAdminRole() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());

        userRepository.save(user);

        mockMvc.perform(post("/admin/users")
                .param("isAdmin", String.valueOf(true))
                .param("id", String.valueOf(user.getId()))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        Set<UserRole> roles = userRepository.findByEmail(user.getEmail()).getRoles();
        Assertions.assertEquals(2, roles.size());
        Assertions.assertTrue(roles.contains(new UserRole(UserRoleEnum.ADMIN)));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testRemovingAdminRole() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());

        userRepository.save(user);

        mockMvc.perform(post("/admin/users")
                        .param("isAdmin", String.valueOf(false))
                        .param("id", String.valueOf(user.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        Set<UserRole> roles = userRepository.findByEmail(user.getEmail()).getRoles();
        Assertions.assertEquals(1, roles.size());
        Assertions.assertFalse(roles.contains(new UserRole(UserRoleEnum.ADMIN)));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddingRoleToInvalidUUId() throws Exception {
        mockMvc.perform(post("/admin/users")
                        .param("isAdmin", String.valueOf(false))
                        .param("id", String.valueOf("sasd"))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddingRoleToNotExistingUser() throws Exception {
        mockMvc.perform(post("/admin/users")
                        .param("isAdmin", String.valueOf(false))
                        .param("id", String.valueOf("3ab0e4c6-0eb7-11ed-861d-0242ac120002"))
                        .with(csrf()))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeletingUser() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());

        userRepository.save(user);
        mockMvc.perform(delete("/admin/users")
                        .param("id", String.valueOf(user.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        Assertions.assertFalse(userRepository.existsByEmail(user.getEmail()));
    }

    @Test
    @WithAnonymousUser
    void testDeleteUserAsNonAdmin() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());

        userRepository.save(user);
        mockMvc.perform(delete("/admin/users")
                        .param("id", String.valueOf(user.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        Assertions.assertTrue(userRepository.existsByEmail(user.getEmail()));
    }

}
