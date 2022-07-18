package com.musicloud.models.dtos;

public class DashboardDto {
    private long users;
    private long songs;
    private long playlists;
    private long newUsers;

    public DashboardDto(long users, long songs, long playlists, long newUsers) {
        this.users = users;
        this.songs = songs;
        this.playlists = playlists;
        this.newUsers = newUsers;
    }

    public long getUsers() {
        return users;
    }

    public long getSongs() {
        return songs;
    }

    public long getPlaylists() {
        return playlists;
    }

    public long getNewUsers() {
        return newUsers;
    }
}
