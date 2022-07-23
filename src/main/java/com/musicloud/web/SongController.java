package com.musicloud.web;

import com.musicloud.models.dtos.song.EditSongDto;
import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.song.SongUploadDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.SongService;
import com.musicloud.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class SongController {

    private SongService songService;
    private UserService userService;

    public SongController(SongService songService, UserService userService) {
        this.songService = songService;
        this.userService = userService;
    }

    @GetMapping("/upload")
    public String getUpload(Model model) {
        if (!model.containsAttribute("songUploadDto"))
            model.addAttribute("songUploadDto", new SongUploadDto());
        return "upload";
    }

    @PostMapping("/upload")
    public String upload(@Valid SongUploadDto songUploadDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("songUploadDto", songUploadDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.songUploadDto", bindingResult);
            return "redirect:/upload";
        }
        songService.add(songUploadDto, userDetails);
        return "redirect:/";
    }

    @GetMapping("/api/songs/{id}")
    @ResponseBody
    public ResponseEntity<SongDto> getSong(@PathVariable("id") UUID songId) {
        SongDto song = songService.getSongById(songId);
        return ResponseEntity.ok(song);
    }

    @PostMapping("/api/songs/{id}/like")
    @ResponseBody
    public ResponseEntity<Boolean> like(@PathVariable("id") UUID songId, @AuthenticationPrincipal AppUserDetails userDetails) {
        userService.likeSong(songId, userDetails);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @DeleteMapping("/api/songs/{id}/like")
    @ResponseBody
    public ResponseEntity<Boolean> dislike(@PathVariable("id") UUID songId, @AuthenticationPrincipal AppUserDetails userDetails) {
        userService.dislikeSong(songId, userDetails);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @ResponseBody
    @PutMapping("/api/songs/{songId}")
    public ResponseEntity<ResponseStatus> editSong(@PathVariable("songId") UUID songId, @Valid EditSongDto songDto, BindingResult bindingResult, @AuthenticationPrincipal AppUserDetails userDetails){
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();
        songService.editSong(songId, songDto, userDetails);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @DeleteMapping("/api/songs/{songId}")
    public ResponseEntity<ResponseStatus> deleteSong(@PathVariable("songId") UUID songId, @AuthenticationPrincipal AppUserDetails userDetails){
        songService.deleteSong(songId, userDetails);
        return ResponseEntity.noContent().build();
    }


}
