package com.musicloud.web;

import com.musicloud.models.Liked;
import com.musicloud.models.Playlist;
import com.musicloud.models.Song;
import com.musicloud.models.User;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.SongRepository;
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
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerIT {
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
        userRepository.deleteAll();
        addTestUser();


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
    void testGetSearchPage() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(view().name("index"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(jsonPath("$.length()").value("1"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(userRepository.findByEmail("admin@test.com").getId())));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetAllUsersWithSearch() throws Exception {
        mockMvc.perform(get("/api/users")
                        .param("q", "admin"))
                .andExpect(jsonPath("$.length()").value("1"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(userRepository.findByEmail("admin@test.com").getId())));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetAllSongs() throws Exception {
        Song song = new Song();
        song.setSongUrl("testSongUrl");
        song.setTitle("testSongTitle");
        song.setCreator(userRepository.findByEmail("admin@test.com"));

        songRepository.save(song);

        mockMvc.perform(get("/api/songs"))
                .andExpect(jsonPath("$.length()").value("1"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(song.getId())));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetAllSongsWithSearch() throws Exception {
        Song song = new Song();
        song.setSongUrl("testSongUrl");
        song.setTitle("testSongTitle");
        song.setCreator(userRepository.findByEmail("admin@test.com"));

        Song song2 = new Song();
        song2.setSongUrl("testSongUrl2");
        song2.setTitle("testSongTitle2");
        song2.setCreator(userRepository.findByEmail("admin@test.com"));

        songRepository.save(song);
        songRepository.save(song2);

        mockMvc.perform(get("/api/songs").param("q", "2"))
                .andExpect(jsonPath("$.length()").value("1"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(song2.getId())));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetAllPlaylists() throws Exception {
        Playlist playlist = new Playlist();
        playlist.setName("testPlaylistName");
        playlist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(playlist);

        mockMvc.perform(get("/api/playlists"))
                .andExpect(jsonPath("$.length()").value("1"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(playlist.getId())));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetAllPlaylistsWithSearch() throws Exception {
        Playlist playlist = new Playlist();
        playlist.setName("testPlaylistName");
        playlist.setUser(userRepository.findByEmail("admin@test.com"));

        Playlist playlist2 = new Playlist();
        playlist2.setName("testPlaylistName2");
        playlist2.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(playlist);
        playlistRepository.save(playlist2);

        mockMvc.perform(get("/api/playlists").param("q", "2"))
                .andExpect(jsonPath("$.length()").value("1"))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(playlist2.getId())));
    }


}
