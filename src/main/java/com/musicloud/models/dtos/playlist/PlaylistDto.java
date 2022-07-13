package com.musicloud.models.dtos.playlist;

import com.musicloud.models.BasePlaylist;
import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.user.CreatorDto;
import com.musicloud.models.principal.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlaylistDto {
    private UUID id;

    private CreatorDto creator;
    private String name;
    private String imageUrl;
    private List<SongDto> songs;

    public PlaylistDto(BasePlaylist basePlaylist) {
        this.id = basePlaylist.getId();
        this.name = basePlaylist.getName();
        if (basePlaylist.getUser() != null){
            this.creator = new CreatorDto(basePlaylist.getUser());
        } else {
            AppUserDetails userDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            this.creator = new CreatorDto(userDetails.getId(), userDetails.getDisplayName());
        }
        this.imageUrl = basePlaylist.getImageUrl();
        this.songs = basePlaylist.getSongs().stream().map(SongDto::new).collect(Collectors.toList());
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

    public CreatorDto getCreator() {
        return creator;
    }
}
