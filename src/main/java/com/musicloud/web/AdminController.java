package com.musicloud.web;

import com.musicloud.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
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
}
