package com.musicloud.models.dtos;

import com.musicloud.validation.ValidSong;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class SongUploadDto {
    @ValidSong
    private MultipartFile songFile;

    private MultipartFile imageFile;

    @NotBlank
    private String title;

    private String tags;

    public MultipartFile getSongFile() {
        return songFile;
    }

    public void setSongFile(MultipartFile songFile) {
        this.songFile = songFile;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
