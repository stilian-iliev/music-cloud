package com.musicloud.models.dtos.song;

import com.musicloud.validation.ValidSong;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class SongUploadDto {
    @ValidSong
    private MultipartFile songFile;

    @NotBlank
    private String title;

    public MultipartFile getSongFile() {
        return songFile;
    }

    public void setSongFile(MultipartFile songFile) {
        this.songFile = songFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
