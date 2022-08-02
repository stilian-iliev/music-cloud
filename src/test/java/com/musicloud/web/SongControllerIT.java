package com.musicloud.web;

import com.musicloud.models.Liked;
import com.musicloud.models.Song;
import com.musicloud.models.User;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SongControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private Song testSong;


    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private StorageService mockStorageService;


    @BeforeEach
    void setUp() {
        addTestUser();
        testSong = new Song();
        testSong.setTitle("testSongTitle");
        testSong.setSongUrl("testSongUrl");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        songRepository.deleteAll();
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
    void getUploadPageAsAnonymous() throws Exception {
        mockMvc.perform(get("/upload"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getUploadPageAsUser() throws Exception {
        mockMvc.perform(get("/upload"))
                .andExpect(status().isOk())
                .andExpect(view().name("upload"));
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testUploadSongAsUser() throws Exception {
        MockMultipartFile testSongFile = new MockMultipartFile("testSongFile", "songFile".getBytes());

        when(mockStorageService.saveSong(any())).thenReturn(Map.of("url", "testSongUrl", "duration", "0"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file("songFile", testSongFile.getBytes())
                        .param("title", "testSongTitle")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Assertions.assertEquals(1, songRepository.count());
    }

    @Test
    @WithAnonymousUser
    void testUploadSongAsAnonymous() throws Exception {
        MockMultipartFile testSongFile = new MockMultipartFile("testSongFile", "songFile".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file("songFile", testSongFile.getBytes())
                        .param("title", "testSongTitle")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(0, songRepository.count());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testUploadSongAsUserWithBadSong() throws Exception {
        MockMultipartFile testSongFile = new MockMultipartFile("testSongFile", "".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file("songFile", testSongFile.getBytes())
                        .param("title", "testSongTitle")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/upload"));

        Assertions.assertEquals(0, songRepository.count());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testUploadSongAsUserWithBadTitle() throws Exception {
        MockMultipartFile testSongFile = new MockMultipartFile("testSongFile", "testSong".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file("songFile", testSongFile.getBytes())
                        .param("title", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/upload"));

        Assertions.assertEquals(0, songRepository.count());
    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGetSongById() throws Exception {
        testSong.setCreator(userRepository.findByEmail("admin@test.com"));
        songRepository.save(testSong);

        mockMvc.perform(get("/api/songs/"+ testSong.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(testSong.getTitle()));

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testLikeSong() throws Exception {
        testSong.setCreator(userRepository.findByEmail("admin@test.com"));
        songRepository.save(testSong);

        mockMvc.perform(post("/api/songs/"+ testSong.getId() + "/like")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(1, userRepository.findByEmail("admin@test.com").getLiked().getSongs().size());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testRemoveLikedSong() throws Exception {
        testSong.setCreator(userRepository.findByEmail("admin@test.com"));
        songRepository.save(testSong);

        mockMvc.perform(post("/api/songs/"+ testSong.getId() + "/like")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(1, userRepository.findByEmail("admin@test.com").getLiked().getSongs().size());

        mockMvc.perform(delete("/api/songs/"+ testSong.getId() + "/like")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, userRepository.findByEmail("admin@test.com").getLiked().getSongs().size());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditASongAsOwner() throws Exception {
        testSong.setCreator(userRepository.findByEmail("admin@test.com"));
        songRepository.save(testSong);

        mockMvc.perform(put("/api/songs/"+ testSong.getId() )
                        .param("title", "changed")
                        .with(csrf()))
                .andExpect(status().isNoContent());


        Assertions.assertEquals("changed", songRepository.findById(testSong.getId()).get().getTitle());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditASongAsOwnerWithBadTitle() throws Exception {
        testSong.setCreator(userRepository.findByEmail("admin@test.com"));
        songRepository.save(testSong);

        mockMvc.perform(put("/api/songs/"+ testSong.getId() )
                        .param("title", "")
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(testSong.getTitle(), songRepository.findById(testSong.getId()).get().getTitle());

    }

    @Test
    @WithAnonymousUser
    void testEditASongAsAnonymous() throws Exception {

        mockMvc.perform(put("/api/songs/"+ testSong.getId() )
                        .param("title", "changed")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());


    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEditASongAsOtherUser() throws Exception {
        User user = new User();
        user.setUsername("owner");
        user.setEmail("owner@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());
        userRepository.save(user);

        testSong.setCreator(user);
        songRepository.save(testSong);

        mockMvc.perform(put("/api/songs/"+ testSong.getId() )
                        .param("title", "changed")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());


        Assertions.assertEquals("testSongTitle", songRepository.findById(testSong.getId()).get().getTitle());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeleteASongAsOwner() throws Exception {
        testSong.setCreator(userRepository.findByEmail("admin@test.com"));
        songRepository.save(testSong);

        mockMvc.perform(delete("/api/songs/"+ testSong.getId() )
                        .with(csrf()))
                .andExpect(status().isNoContent());


        Assertions.assertEquals(0, songRepository.count());

    }

    @Test
    @WithUserDetails(value = "admin@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeleteASongAsOtherUser() throws Exception {
        User user = new User();
        user.setUsername("owner");
        user.setEmail("owner@test.com");
        user.setPassword(passwordEncoder.encode("topsecret"));
        user.setImageUrl("imageUrl");
        user.setLiked(new Liked());
        user.addRole(roleRepository.findById(1L).get());
        user.addRole(roleRepository.findById(2L).get());
        userRepository.save(user);

        testSong.setCreator(user);
        songRepository.save(testSong);

        mockMvc.perform(delete("/api/songs/"+ testSong.getId() )
                        .with(csrf()))
                .andExpect(status().isUnauthorized());


        Assertions.assertEquals(1, songRepository.count());

    }
}
