package com.musicloud.services;

import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.repositories.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public List<PlaylistDto> findAllByUserId(UUID userId) {
        return playlistRepository.findAllByUserId(userId).stream().map(PlaylistDto::new).collect(Collectors.toList());
    }
}
