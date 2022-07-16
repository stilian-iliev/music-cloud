package com.musicloud.models.dtos.user;

import com.musicloud.models.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserSummaryDto {
    private String fullName;

    private UUID id;
    private String email;
    private String imageUrl;
    private List<String > roles;

    public UserSummaryDto(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.roles = user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList());
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getRoles() {
        return roles;
    }
}
