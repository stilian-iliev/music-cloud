package com.musicloud.models.dtos;

import org.springframework.web.multipart.MultipartFile;

public class SongUploadDto {
    private MultipartFile songFile;

    private MultipartFile imageFile;

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
