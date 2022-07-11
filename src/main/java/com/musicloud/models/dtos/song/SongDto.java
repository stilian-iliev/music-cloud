package com.musicloud.models.dtos.song;

import com.musicloud.models.Song;
import com.musicloud.models.dtos.user.CreatorDto;

import java.util.UUID;

public class SongDto {
    private UUID id;
    private CreatorDto creator;
    private String title;
    private String songUrl;
    private int duration;

    public SongDto(Song song) {
        this.id = song.getId();
        this.creator = new CreatorDto(song.getCreator());
        this.title = song.getTitle();
        this.songUrl = song.getSongUrl();
        this.duration = song.getDuration();
    }

    public UUID getId() {
        return id;
    }

    public CreatorDto getCreator() {
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
