package com.musicloud.services;

import com.musicloud.models.Song;
import com.musicloud.models.User;
import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.song.SongUploadDto;
import com.musicloud.models.exceptions.SongNotFoundException;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.SongRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final UserService userService;
    private final StorageService storageService;
    private final ModelMapper mapper;

    public SongService(SongRepository songRepository, UserService userService, StorageService storageService, ModelMapper mapper) {
        this.songRepository = songRepository;
        this.userService = userService;
        this.storageService = storageService;
        this.mapper = mapper;
    }


    public void add(SongUploadDto songUploadDto, AppUserDetails userDetails) throws IOException {
        Song song = mapper.map(songUploadDto, Song.class);
        Map<String, Integer> songInfo = storageService.saveSong(songUploadDto.getSongFile());
        song.setSongUrl(songInfo.keySet().stream().findFirst().orElseThrow());
        song.setDuration(songInfo.values().stream().findFirst().orElseThrow());
        User user = userService.getUserById(userDetails.getId());
        song.setCreator(user);

        songRepository.save(song);
    }

    public SongDto getSongById(UUID songId) {
        return songRepository.findByIdOrderByCreationTime(songId).map(SongDto::new).orElseThrow(SongNotFoundException::new);
    }

    public List<SongDto> getAllSongs() {
        return songRepository.findAll().stream().map(SongDto::new).collect(Collectors.toList());
    }
}
