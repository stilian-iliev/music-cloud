package com.musicloud.models.dtos.song;

import com.musicloud.models.Song;

import java.util.UUID;

public class SongDto {
    private UUID id;
    private String creator;
    private String title;
    private String songUrl;
    private int duration;

    public SongDto(Song song) {
        this.id = song.getId();
        this.creator = song.getCreator().getUsername();
        this.title = song.getTitle();
        this.songUrl = song.getSongUrl();
        this.duration = song.getDuration();
    }

    public UUID getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public int getDuration() {
        return duration;
    }
}
