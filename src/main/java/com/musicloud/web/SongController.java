package com.musicloud.web;

import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.song.SongUploadDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.SongService;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class SongController {

    private SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
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

    @GetMapping("/api/users/{id}/songs")
    @ResponseBody
    public ResponseEntity<List<SongDto>> getUserSongs(@PathVariable("id") UUID userId) {
        List<SongDto> songsByUserId = songService.getSongsByUserId(userId);
        return ResponseEntity.ok(songsByUserId);
    }

    @GetMapping("/api/songs/{id}")
    @ResponseBody
    public ResponseEntity<SongDto> getSong(@PathVariable("id") UUID songId) {
        SongDto song = songService.getSongById(songId);
        return ResponseEntity.ok(song);
    }

    //todo:get search and pagable
//    @GetMapping("/api/songs?page={page}")
//    @ResponseBody
//    public ResponseEntity<List<SongDto>> getAllSongs() {
//        List<SongDto> allSongs = songService.getAllSongs();
//        return ResponseEntity.ok(allSongs);
//    }

}
