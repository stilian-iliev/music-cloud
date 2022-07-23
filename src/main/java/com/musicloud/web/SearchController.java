package com.musicloud.web;

import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.user.UserCardDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.PlaylistService;
import com.musicloud.services.SongService;
import com.musicloud.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController {
    private final UserService userService;
    private final SongService songService;
    private final PlaylistService playlistService;

    public SearchController(UserService userService, SongService songService, PlaylistService playlistService) {

        this.userService = userService;
        this.songService = songService;
        this.playlistService = playlistService;
    }

    @GetMapping("/search")
    public String getSearch() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/api/users")
    public ResponseEntity<List<UserCardDto>> getAllUsers(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(required = false, value = "q") String query) {

        return ResponseEntity.ok(userService.getAllUserCards(userDetails.getId(),query));
    }

    @GetMapping("/api/songs")
    @ResponseBody
    public ResponseEntity<List<SongDto>> getAllSongs(@RequestParam(required = false, value = "q") String query) {
        List<SongDto> allSongs = songService.getAllSongs(query);
        return ResponseEntity.ok(allSongs);
    }

    @ResponseBody
    @GetMapping("/api/playlists")
    public ResponseEntity<List<PlaylistDto>> getAllPlaylists(@RequestParam(required = false, value = "q") String query) {
        return ResponseEntity.ok(playlistService.findAll(query));
    }
}
