package com.musicloud.web;

import com.musicloud.models.BasePlaylist;
import com.musicloud.models.Liked;
import com.musicloud.models.Playlist;
import com.musicloud.models.User;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class PlaylistControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaylistRepository playlistRepository;

    private Playlist testPlaylist;

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
        Playlist playlist = new Playlist();
        playlist.setName("testPlaylist");
        testPlaylist = playlistRepository.save(playlist);
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
    void testGetPlaylistPage() throws Exception {
        mockMvc.perform(get("/playlist/" + testPlaylist.getId()))
                .andExpect(view().name("index"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetLikedPage() throws Exception {
        mockMvc.perform(get("/liked"))
                .andExpect(view().name("index"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testCreatingPlaylist() throws Exception {
        mockMvc.perform(post("/playlists/create")
                        .param("name", "testPlaylist")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testCreatingPlaylistWithoutName() throws Exception {
        mockMvc.perform(post("/playlists/create")
                        .param("name", "")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetPlaylist() throws Exception {
        mockMvc.perform(get("/api/playlists/"+ testPlaylist.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetPlaylistWithInvalidUUID() throws Exception {
        mockMvc.perform(get("/api/playlists/"+ "invalidUUID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetPlaylistThatDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/playlists/"+ "5048a6d4-0e8a-11ed-861d-0242ac120002"))
                .andExpect(status().isNotFound());
    }
}
