package com.musicloud.web;

import com.musicloud.models.Liked;
import com.musicloud.models.Playlist;
import com.musicloud.models.Song;
import com.musicloud.models.User;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.SongRepository;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import com.musicloud.services.StorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerIT {
    @MockBean
    private StorageService mockStorageService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        addTestUser();
        playlistRepository.deleteAll();

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        songRepository.deleteAll();
        playlistRepository.deleteAll();
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
    void testGetProfilePage() throws Exception {
        mockMvc.perform(get("/user/" + userRepository.findByEmail("admin@test.com").getId()))
                .andExpect(view().name("index"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditingProfile() throws Exception {
        mockMvc.perform(put("/profile/edit")
                .param("firstName", "firstName")
                .param("lastName", "lastName")
                .param("username", "username")
                .with(csrf()))
                .andExpect(status().isOk());
        User user = userRepository.findByEmail("admin@test.com");

        Assertions.assertEquals("firstName", user.getFirstName());
        Assertions.assertEquals("lastName", user.getLastName());
        Assertions.assertEquals("username", user.getUsername());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditingProfileWithBadUsername() throws Exception {
        mockMvc.perform(put("/profile/edit")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .with(csrf()))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetUserById() throws Exception {
        User user = userRepository.findByEmail("admin@test.com");
        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(jsonPath("$.imageUrl").value(user.getImageUrl()));

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetCurrentUserId() throws Exception {
        User user = userRepository.findByEmail("admin@test.com");
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("\"%s\"", user.getId())));

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetUserLikedPlaylist() throws Exception {
        User user = userRepository.findByEmail("admin@test.com");

        mockMvc.perform(get("/api/liked"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(String.valueOf(user.getLiked().getId())));

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetUserPlaylists() throws Exception {
        User user = userRepository.findByEmail("admin@test.com");
        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setName("testPlaylist");
        playlistRepository.save(playlist);

        mockMvc.perform(get("/api/users/" + user.getId() + "/playlists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(playlist.getId().toString()))
        ;

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetUserSongs() throws Exception {
        User user = userRepository.findByEmail("admin@test.com");
        Song song = new Song();
        song.setSongUrl("testSongUrl");
        song.setTitle("testSongTitle");
        song.setCreator(user);
        songRepository.save(song);

        mockMvc.perform(get("/api/users/" + user.getId() + "/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(song.getId().toString()))
        ;

    }

//    @Test
//    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    void testGetUserFollowedUsers() throws Exception {
//        User user = userRepository.findByEmail("admin@test.com");
//        User user2 = new User();
//        user.setUsername("user");
//        user.setEmail("user@test.com");
//        user.setPassword(passwordEncoder.encode("topsecret"));
//        user.setImageUrl("imageUrl");
//        user.setLiked(new Liked());
//        user.addRole(roleRepository.findById(1L).get());
//
//        user.followUser(user2);
//
//        userRepository.save(user);
//        userRepository.save(user2);
//
//        mockMvc.perform(get("/api/users/" + user.getId() + "/songs"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1))
//                .andExpect(jsonPath("$.[0].id").value(song.getId().toString()))
//        ;
//
//    }
}
