package com.musicloud.models.dtos.song;

import javax.validation.constraints.NotBlank;

public class EditSongDto {
    @NotBlank
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
