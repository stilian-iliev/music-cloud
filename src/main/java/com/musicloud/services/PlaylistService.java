package com.musicloud.services;

import com.musicloud.models.Playlist;
import com.musicloud.models.User;
import com.musicloud.models.dtos.playlist.PlaylistCreateDto;
import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final ModelMapper mapper;

    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository, StorageService storageService, ModelMapper mapper) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.mapper = mapper;
    }

    public List<PlaylistDto> findAllByUserId(UUID userId) {
        return playlistRepository.findAllByUserIdOrderByCreationTime(userId).stream().map(PlaylistDto::new).collect(Collectors.toList());
    }

    public PlaylistDto findById(UUID playlistDto) {
        return playlistRepository.findById(playlistDto).map(PlaylistDto::new).orElseThrow();
    }

    @Transactional
    public PlaylistDto findLiked(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Playlist liked = user.getLiked();
        return new PlaylistDto(liked);
    }

    public PlaylistDto create(PlaylistCreateDto playlistCreateDto, AppUserDetails userDetails) throws IOException {
        Playlist playlist = mapper.map(playlistCreateDto, Playlist.class);
        playlist.setUser(userRepository.findById(userDetails.getId()).orElseThrow());
        String image = storageService.saveImage(playlistCreateDto.getImage());
        playlist.setImageUrl(image != null ? image : "https://res.cloudinary.com/dtzjbyjzq/image/upload/v1657567072/images/1482975da7275050a3a8406f90c4610d_f9qkkc.jpg");
        playlistRepository.save(playlist);
        return new PlaylistDto(playlist);
    }
}
