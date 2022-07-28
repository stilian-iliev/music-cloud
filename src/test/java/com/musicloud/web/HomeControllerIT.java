package com.musicloud.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void testGetLandingPageForAnonymousUser() throws Exception {
        mockMvc.perform(get("/")).andExpect(view().name("welcome"));
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
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
    @WithUserDetails("admin@musicloud.com")
    void testGetLibraryPageForLoggedInUser() throws Exception {
        mockMvc.perform(get("/library")).andExpect(view().name("index"));
    }
}

//TODO: unit-> dashboard
