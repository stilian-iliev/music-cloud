package com.musicloud.models.dtos.user;

import com.musicloud.models.User;
import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.dtos.song.SongDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String username;
    private String fullName;
    private String imageUrl;

    private int following;
    private int followers;

    public UserProfileDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.imageUrl = user.getImageUrl();
        this.following = user.getFollowedUsers().size();
        this.followers = user.getFollowers().size();
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

    public int getFollowing() {
        return following;
    }

    public int getFollowers() {
        return followers;
    }
}
