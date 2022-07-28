package com.musicloud.web;

import com.musicloud.models.BasePlaylist;
import com.musicloud.models.Playlist;
import com.musicloud.repositories.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @BeforeEach
    void setUp() {
        playlistRepository.deleteAll();
        Playlist playlist = new Playlist();
        playlist.setName("testPlaylist");
        testPlaylist = playlistRepository.save(playlist);
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void testGetPlaylistPage() throws Exception {
        mockMvc.perform(get("/playlist/" + testPlaylist.getId()))
                .andExpect(view().name("index"));
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void testGetLikedPage() throws Exception {
        mockMvc.perform(get("/liked"))
                .andExpect(view().name("index"));
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void testCreatingPlaylist() throws Exception {
        mockMvc.perform(post("/playlists/create")
                        .param("name", "testPlaylist")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void testCreatingPlaylistWithoutName() throws Exception {
        mockMvc.perform(post("/playlists/create")
                        .param("name", "")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void testGetPlaylist() throws Exception {
        mockMvc.perform(get("/api/playlists/"+ testPlaylist.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void testGetPlaylistWithInvalidUUID() throws Exception {
        mockMvc.perform(get("/api/playlists/"+ "invalidUUID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void testGetPlaylistThatDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/playlists/"+ "5048a6d4-0e8a-11ed-861d-0242ac120002"))
                .andExpect(status().isNotFound());
    }
}
