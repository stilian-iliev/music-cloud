package com.musicloud.models.dtos.user;

import com.musicloud.models.User;

import java.util.UUID;

public class CreatorDto {
    private UUID id;
    private String username;

    public CreatorDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public CreatorDto(UUID id, String displayName) {
        this.id = id;
        this.username = displayName;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
