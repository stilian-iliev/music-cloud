package com.musicloud.web;

import com.musicloud.models.*;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.SongRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PlaylistControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private SongRepository songRepository;

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
       testPlaylist = new Playlist();
        testPlaylist.setName("testPlaylist");

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
    void testGetPlaylistPage() throws Exception {
        playlistRepository.save(testPlaylist);
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
        mockMvc.perform(post("/api/playlists")
                        .param("name", "testPlaylist")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testCreatingPlaylistWithoutName() throws Exception {
        mockMvc.perform(post("/api/playlists")
                        .param("name", "")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetPlaylist() throws Exception {
        playlistRepository.save(testPlaylist);
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

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddSongToPlaylist() throws Exception {

        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);

        Song song = new Song();
        song.setTitle("testSong");
        song.setSongUrl("testSongUrl");
        song.setCreator(userRepository.findByEmail("admin@test.com"));

        songRepository.save(song);

        mockMvc.perform(post("/api/playlists/"+ testPlaylist.getId() + "/songs/"+ song.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(1, playlistRepository.findById(testPlaylist.getId()).get().getSongs().size());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddSongToPlaylistAsUnauthorized() throws Exception {
        User user = new User();
        user.setUsername("authorized");
        user.setEmail("authorized@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());

        userRepository.save(user);
        testPlaylist.setUser(user);

        playlistRepository.save(testPlaylist);

        Song song = new Song();
        song.setTitle("testSong");
        song.setSongUrl("testSongUrl");
        song.setCreator(user);

        songRepository.save(song);

        mockMvc.perform(post("/api/playlists/"+ testPlaylist.getId() + "/songs/"+ song.getId())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        Assertions.assertEquals(0, playlistRepository.findById(testPlaylist.getId()).get().getSongs().size());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddInvalidSongToPlaylist() throws Exception {
        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);

        mockMvc.perform(post("/api/playlists/"+ testPlaylist.getId() + "/songs/invalidUUid")
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, playlistRepository.findById(testPlaylist.getId()).get().getSongs().size());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAddNotExistingSongToPlaylist() throws Exception {
        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);

        mockMvc.perform(post("/api/playlists/"+ testPlaylist.getId() + "/songs/5048a6d4-0e8a-11ed-861d-0242ac120002")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(0, playlistRepository.findById(testPlaylist.getId()).get().getSongs().size());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testRemoveSongToPlaylist() throws Exception {
        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);

        Song song = new Song();
        song.setTitle("testSong");
        song.setSongUrl("testSongUrl");
        song.setCreator(userRepository.findByEmail("admin@test.com"));

        songRepository.save(song);
        testPlaylist.addSong(song);

        playlistRepository.save(testPlaylist);

        mockMvc.perform(delete("/api/playlists/"+ testPlaylist.getId() + "/songs/"+ song.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, playlistRepository.findById(testPlaylist.getId()).get().getSongs().size());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testRemoveInvalidSongToPlaylist() throws Exception {
        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);

        mockMvc.perform(delete("/api/playlists/"+ testPlaylist.getId() + "/songs/invalidUUid")
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, playlistRepository.findById(testPlaylist.getId()).get().getSongs().size());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testRemoveNotExistingSongToPlaylist() throws Exception {
        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);

        mockMvc.perform(delete("/api/playlists/"+ testPlaylist.getId() + "/songs/5048a6d4-0e8a-11ed-861d-0242ac120002")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(0, playlistRepository.findById(testPlaylist.getId()).get().getSongs().size());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditingPlaylist() throws Exception {
        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);

        mockMvc.perform(put("/api/playlists/"+ testPlaylist.getId())
                        .param("name", "changed")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals("changed", playlistRepository.findById(testPlaylist.getId()).get().getName());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditingPlaylistAsUnauthorized() throws Exception {
        User user = new User();
        user.setUsername("authorized");
        user.setEmail("authorized@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());

        userRepository.save(user);
        testPlaylist.setUser(user);

        playlistRepository.save(testPlaylist);

        mockMvc.perform(put("/api/playlists/"+ testPlaylist.getId())
                        .param("name", "changed")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        Assertions.assertEquals("testPlaylist", playlistRepository.findById(testPlaylist.getId()).get().getName());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditingPlaylistWithBadArgs() throws Exception {
        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);

        mockMvc.perform(put("/api/playlists/"+ testPlaylist.getId())
                        .param("name", "")
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals("testPlaylist", playlistRepository.findById(testPlaylist.getId()).get().getName());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditingNotExistingPlaylist() throws Exception {
        mockMvc.perform(put("/api/playlists/5048a6d4-0e8a-11ed-861d-0242ac120002")
                        .param("name", "changed")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditingInvalidPlaylist() throws Exception {
        mockMvc.perform(put("/api/playlists/invalid")
                        .param("name", "changed")
                        .with(csrf()))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeletingPlaylist() throws Exception {
        testPlaylist.setUser(userRepository.findByEmail("admin@test.com"));

        playlistRepository.save(testPlaylist);
        Assertions.assertEquals(1, playlistRepository.count());
        mockMvc.perform(delete("/api/playlists/" + testPlaylist.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, playlistRepository.count());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeletingPlaylistAsUnauthorized() throws Exception {
        User user = new User();
        user.setUsername("authorized");
        user.setEmail("authorized@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());

        userRepository.save(user);
        testPlaylist.setUser(user);

        playlistRepository.save(testPlaylist);

        mockMvc.perform(delete("/api/playlists/" + testPlaylist.getId())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        Assertions.assertEquals(1, playlistRepository.count());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeletingNotExistingPlaylist() throws Exception {
        mockMvc.perform(delete("/api/playlists/5048a6d4-0e8a-11ed-861d-0242ac120002")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(0, playlistRepository.count());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeletingInvalidPlaylist() throws Exception {
        mockMvc.perform(delete("/api/playlists/invalidUUid")
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0, playlistRepository.count());
    }
}
