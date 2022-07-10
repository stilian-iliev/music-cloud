package com.musicloud.models.dtos.song;

import com.musicloud.models.Song;

import java.util.UUID;

public class SongDto {
    private UUID creatorId;
    private String title;
    private String songUrl;

    public SongDto(Song song) {
        this.creatorId = song.getCreator().getId();
        this.title = song.getTitle();
        this.songUrl = song.getSongUrl();
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public String getTitle() {
        return title;
    }

    public String getSongUrl() {
        return songUrl;
    }
}
