package com.musicloud.models.dtos.playlist;

import com.musicloud.models.Playlist;
import com.musicloud.models.dtos.song.SongDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlaylistDto {
    private UUID id;
    private String name;
    private String imageUrl;
    private List<SongDto> songs;

    public PlaylistDto(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.imageUrl = playlist.getImageUrl();
        this.songs = playlist.getSongs().stream().map(SongDto::new).collect(Collectors.toList());
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<SongDto> getSongs() {
        return songs;
    }
}
