package com.musicloud.web;

import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public String getProfile(@PathVariable("userId") UUID userId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        EditProfileDto editProfileDto = userService.getEditProfileDto(userDetails.getUsername());
        model.addAttribute("editProfileDto", editProfileDto);
        return "profile-details";
    }

    @PostMapping("/profile/edit")
    public String editProfile(EditProfileDto editProfileDto, @AuthenticationPrincipal AppUserDetails userDetails) {
        userService.editProfile(editProfileDto, userDetails);
        return "redirect:/user/"+userDetails.getId();
    }



}
