package com.musicloud.services;

import com.musicloud.models.Playlist;
import com.musicloud.models.dtos.playlist.PlaylistCreateDto;
import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.exceptions.PlaylistNotFoundException;
import com.musicloud.models.exceptions.SongNotFoundException;
import com.musicloud.models.exceptions.UnauthorizedException;
import com.musicloud.models.exceptions.UserNotFoundException;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.SongRepository;
import com.musicloud.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final ModelMapper mapper;

    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository, UserRepository userRepository, StorageService storageService, ModelMapper mapper) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.mapper = mapper;
    }


    public PlaylistDto findById(UUID playlistDto) {
        return playlistRepository.findById(playlistDto).map(PlaylistDto::new).orElseThrow(PlaylistNotFoundException::new);
    }

    public PlaylistDto create(PlaylistCreateDto playlistCreateDto, AppUserDetails userDetails) throws IOException {
        Playlist playlist = mapper.map(playlistCreateDto, Playlist.class);
        playlist.setUser(userRepository.findById(userDetails.getId()).orElseThrow(UserNotFoundException::new));
        String image = storageService.saveImage(playlistCreateDto.getImage());
        playlist.setImageUrl(image != null ? image : "https://res.cloudinary.com/dtzjbyjzq/image/upload/v1657567072/images/1482975da7275050a3a8406f90c4610d_f9qkkc.jpg");
        playlistRepository.save(playlist);
        return new PlaylistDto(playlist);
    }

    public void addSongToPlaylist(UUID playlistId, UUID songId, AppUserDetails userDetails) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(PlaylistNotFoundException::new);
        if (!playlist.getUser().getId().equals(userDetails.getId())) throw new UnauthorizedException();

        playlist.addSong(songRepository.findById(songId).orElseThrow(SongNotFoundException::new));

        playlistRepository.save(playlist);
    }

    public void removeSongFromPlaylist(UUID playlistId, UUID songId, AppUserDetails userDetails) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(PlaylistNotFoundException::new);
        if (!playlist.getUser().getId().equals(userDetails.getId())) throw new UnauthorizedException();

        playlist.removeSong(songRepository.findById(songId).orElseThrow(SongNotFoundException::new));

        playlistRepository.save(playlist);
    }

    public void editPlaylist(UUID playlistId, PlaylistCreateDto playlistDto, AppUserDetails userDetails) throws IOException {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(PlaylistNotFoundException::new);
        if (!playlist.getUser().getId().equals(userDetails.getId())) throw new UnauthorizedException();
        playlist.setName(playlistDto.getName());
        if (!playlistDto.getImage().isEmpty()) {
            String image = storageService.saveImage(playlistDto.getImage());
            playlist.setImageUrl(image);
        }

        playlistRepository.save(playlist);
    }
}
