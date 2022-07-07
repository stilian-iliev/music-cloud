package com.musicloud.web;

import com.musicloud.models.User;
import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public String getProfile(@PathVariable("userId") UUID userId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile-details";
    }

    @PostMapping("/profile/edit")
    public String editProfile(EditProfileDto editProfileDto, @AuthenticationPrincipal AppUserDetails userDetails) throws IOException {
        userService.editProfile(editProfileDto, userDetails);
        return "redirect:/user/" + userDetails.getId();
    }


}
