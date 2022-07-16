package com.musicloud.web;

import com.musicloud.services.AuthService;
import com.musicloud.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class AdminController {
    private final AuthService authService;
    private final UserService userService;

    public AdminController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getPage() {
        return "admin_dashboard";
    }
    @GetMapping("/admin/users")
    public String getUserManagement(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin_users";
    }

    @DeleteMapping("/admin/users")
    public String deleteUser(UUID id) {
        System.out.println(id);
//        authService.deleteUser();
        return "index";
    }
}
