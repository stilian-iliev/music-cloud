package com.musicloud.web;

import com.musicloud.models.dtos.user.UserSummaryDto;
import com.musicloud.models.enums.UserRoleEnum;
import com.musicloud.services.AuthService;
import com.musicloud.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.stream.Collectors;

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
        model.addAttribute("users", userService.getAllUsers().stream().map(UserSummaryDto::new).collect(Collectors.toList()));
        return "admin_users";
    }

    @DeleteMapping("/admin/users")
    public String deleteUser(@RequestParam("id") UUID id) {
        authService.deleteAccount(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users")
    public String editRoles(@RequestParam(value = "isAdmin", required = false) boolean checked, @RequestParam(value = "id") UUID userId) {
        if (checked) {
            authService.addRole(userId, UserRoleEnum.ADMIN);
        } else {
            authService.removeRole(userId, UserRoleEnum.ADMIN);
        }
        return "redirect:/admin/users";
    }
}
