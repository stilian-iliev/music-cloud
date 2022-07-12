package com.musicloud.web;

import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.user.EditProfileDto;
import com.musicloud.models.dtos.user.UserProfileDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public String getProfile(@PathVariable("userId") UUID userId, Model model, @AuthenticationPrincipal AppUserDetails userDetails) {
        return "index";
    }

    @ResponseBody
    @PutMapping("/profile/edit")
    public ResponseEntity<UserProfileDto> editProfile(EditProfileDto editProfileDto, @AuthenticationPrincipal AppUserDetails userDetails) throws IOException {
        userService.editProfile(editProfileDto, userDetails);
        UserProfileDto profileDto = userService.getProfileDto(userDetails.getId());
        return ResponseEntity.ok(profileDto);
    }

    @ResponseBody
    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserProfileDto> getUser(@PathVariable("id") UUID userId) {
        UserProfileDto profileDto = userService.getProfileDto(userId);
        return ResponseEntity.ok(profileDto);
    }

    @ResponseBody
    @GetMapping("/api/me")
    public ResponseEntity<UUID> getMyId(@AuthenticationPrincipal AppUserDetails appUserDetails) {
        return ResponseEntity.ok(appUserDetails.getId());
    }

    @ResponseBody
    @GetMapping("/api/liked")
    public ResponseEntity<PlaylistDto> getLiked(@AuthenticationPrincipal AppUserDetails userDetails) {
        PlaylistDto playlist = userService.findLiked(userDetails.getId());
        return ResponseEntity.ok(playlist);
    }

    @ResponseBody
    @GetMapping("/api/users/{id}/playlists")
    public ResponseEntity<List<PlaylistDto>> getUserPlaylists(@PathVariable("id")UUID userId) {
        List<PlaylistDto> playlists = userService.findPlaylistsOfUser(userId);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/api/users/{id}/songs")
    @ResponseBody
    public ResponseEntity<List<SongDto>> getUserSongs(@PathVariable("id") UUID userId) {
        List<SongDto> songsByUserId = userService.getSongsByUserId(userId);
        return ResponseEntity.ok(songsByUserId);
    }
}
