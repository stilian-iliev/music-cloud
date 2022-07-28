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

import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void getPlaylistPage() throws Exception {
        mockMvc.perform(get("/playlist/" + testPlaylist.getId()))
                .andExpect(view().name("index"));
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void getLikedPage() throws Exception {
        mockMvc.perform(get("/liked"))
                .andExpect(view().name("index"));
    }
}
