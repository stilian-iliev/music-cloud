package com.musicloud.web;

import com.musicloud.models.User;
import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.models.dtos.UserProfileDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.SongService;
import com.musicloud.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Controller
public class ProfileController {

    private final UserService userService;
    private final SongService songService;

    public ProfileController(UserService userService, SongService songService) {
        this.userService = userService;
        this.songService = songService;
    }

    @GetMapping("/user/{userId}")
    public String getProfile(@PathVariable("userId") UUID userId, Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("userSongs", songService.getSongsByUserId(userDetails.getId()));
        return "profile-details";
    }

    @PostMapping("/profile/edit")
    public String editProfile(EditProfileDto editProfileDto, @AuthenticationPrincipal AppUserDetails userDetails) throws IOException {
        userService.editProfile(editProfileDto, userDetails);
        return "redirect:/user/" + userDetails.getId();
    }

    @ResponseBody
    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserProfileDto> getUser(@PathVariable("id") UUID userId) {
        UserProfileDto profileDto = userService.getProfileDto(userId);
        return ResponseEntity.ok(profileDto);
    }
}
