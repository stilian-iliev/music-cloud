package com.musicloud.web;

import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.services.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

//    @ResponseBody
//    @PostMapping("/playlists/create")
//    public ResponseEntity<List<PlaylistDto>> getUserPlaylists(@PathVariable("id")UUID userId) {
//        List<PlaylistDto> playlists = playlistService.findAllByUserId(userId);
//        return ResponseEntity.ok(playlists);
//    }
    @ResponseBody
    @GetMapping("/api/users/{id}/playlists")
    public ResponseEntity<List<PlaylistDto>> getUserPlaylists(@PathVariable("id")UUID userId) {
        List<PlaylistDto> playlists = playlistService.findAllByUserId(userId);
        return ResponseEntity.ok(playlists);
    }
}
