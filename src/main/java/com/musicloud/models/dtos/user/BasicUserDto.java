package com.musicloud.models.dtos.user;

import com.musicloud.models.User;

import java.util.UUID;

public class BasicUserDto {
    private String username;
    private String imageUrl;
    private UUID id;

    public BasicUserDto(User user) {
        this.username = user.getUsername();
        this.imageUrl = user.getImageUrl();
        this.id = user.getId();
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
}
