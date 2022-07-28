package com.musicloud.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("admin@musicloud.com")
    void getAdminDashboardPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(view().name("admin_dashboard"));
    }

    @Test
    @WithUserDetails("admin@musicloud.com")
    void getAdminUsersPage() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(view().name("admin_users"));
    }
    //todo not adminUser
}
