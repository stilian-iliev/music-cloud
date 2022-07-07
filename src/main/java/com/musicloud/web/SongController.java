package com.musicloud.web;

import com.musicloud.models.dtos.SongUploadDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.SongService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class SongController {

    private SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/upload")
    public String getUpload() {
        return "upload";
    }

    @PostMapping("/upload")
    public String upload(@Valid SongUploadDto songUploadDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) throws IOException {
        if (bindingResult.hasErrors()){
            return "redirect:/upload";
        }
        songService.add(songUploadDto, userDetails);
        return "redirect:/";
    }
}
