package com.musicloud.models.dtos;

public class SongDto {
    private String title;

    private String songUrl;

    public SongDto(String title, String songUrl) {
        this.title = title;
        this.songUrl = songUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}
