package com.musicloud.models.dtos.user;

import com.musicloud.models.User;

import java.util.UUID;

public class UserCardDto {
    private String username;
    private String imageUrl;
    private UUID id;
    private boolean followed;

    public UserCardDto(User user, User current) {
        this.username = user.getUsername();
        this.imageUrl = user.getImageUrl();
        this.id = user.getId();
        this.followed = current.getFollowedUsers().stream().anyMatch(u -> u == user);
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UUID getId() {
        return id;
    }

    public boolean isFollowed() {
        return followed;
    }
}
