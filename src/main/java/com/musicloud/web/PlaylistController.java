package com.musicloud.web;

import com.musicloud.models.dtos.playlist.PlaylistCreateDto;
import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/playlist/{id}")
    public String getPlaylistPage() {
        return "index";
    }

    @GetMapping("/liked")
    public String getLikedPage() {
        return "index";
    }

    @ResponseBody
    @PostMapping("/playlists/create")
    public ResponseEntity<PlaylistDto> createPlaylist(@Valid PlaylistCreateDto playlistCreateDto, BindingResult bindingResult, @AuthenticationPrincipal AppUserDetails userDetails) throws IOException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        PlaylistDto playlist = playlistService.create(playlistCreateDto, userDetails);
        return ResponseEntity.ok(playlist);
    }

    @ResponseBody
    @GetMapping("/api/playlists/{id}")
    public ResponseEntity<PlaylistDto> getPlaylist(@PathVariable("id")UUID playlistDto) {
        PlaylistDto playlist = playlistService.findById(playlistDto);
        return ResponseEntity.ok(playlist);
    }

    @ResponseBody
    @PostMapping("/api/playlists/{playlistId}/songs/{songId}")
    public ResponseEntity<ResponseStatus> addSongToPlaylist(@PathVariable("playlistId")UUID playlistId, @PathVariable("songId") UUID songId, @AuthenticationPrincipal AppUserDetails userDetails) {
        playlistService.addSongToPlaylist(playlistId, songId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @DeleteMapping("/api/playlists/{playlistId}/songs/{songId}")
    public ResponseEntity<ResponseStatus> removeSongFromPlaylist(@PathVariable("playlistId")UUID playlistId, @PathVariable("songId") UUID songId, @AuthenticationPrincipal AppUserDetails userDetails) {
        playlistService.removeSongFromPlaylist(playlistId, songId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @PutMapping("/api/playlists/{playlistId}")
    public ResponseEntity<ResponseStatus> editPlaylist(@PathVariable("playlistId")UUID playlistId, @Valid PlaylistCreateDto playlistDto, BindingResult bindingResult, @AuthenticationPrincipal AppUserDetails userDetails) throws IOException {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();
        playlistService.editPlaylist(playlistId, playlistDto, userDetails);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @DeleteMapping("/api/playlists/{playlistId}")
    public ResponseEntity<ResponseStatus> deletePlaylist(@PathVariable("playlistId")UUID playlistId, @AuthenticationPrincipal AppUserDetails userDetails) {
        playlistService.deletePlaylist(playlistId, userDetails);
        return ResponseEntity.noContent().build();
    }



}
