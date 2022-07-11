package com.musicloud.models.dtos.playlist;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class PlaylistCreateDto {
    @NotBlank
    private String name;
    private MultipartFile image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
