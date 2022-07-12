package com.musicloud.web;

import com.musicloud.models.dtos.playlist.PlaylistCreateDto;
import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@Controller
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/playlist/{id}")
    public String getPage() {
        return "index";
    }

    @ResponseBody
    @PostMapping("/playlists/create")
    public ResponseEntity<PlaylistDto> createPlaylist(@Valid PlaylistCreateDto playlistCreateDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) throws IOException {
        PlaylistDto playlist = playlistService.create(playlistCreateDto, userDetails);
        return ResponseEntity.ok(playlist);
    }

    @ResponseBody
    @GetMapping("/api/playlists/{id}")
    public ResponseEntity<PlaylistDto> getPlaylist(@PathVariable("id")UUID playlistDto) {
        PlaylistDto playlist = playlistService.findById(playlistDto);
        return ResponseEntity.ok(playlist);
    }
}
