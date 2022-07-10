package com.musicloud.models.dtos;

import com.musicloud.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String username;
    private String fullName;
    private String imageUrl;
    private List<SongDto> songs;
    private List<PlaylistDto> playlists;

    public UserProfileDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.imageUrl = user.getImageUrl();
        this.songs = user.getSongs().stream().map(SongDto::new).collect(Collectors.toList());
        //todo
        this.playlists = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<SongDto> getSongs() {
        return songs;
    }

    public List<PlaylistDto> getPlaylists() {
        return playlists;
    }
}
