package com.musicloud.services;

import com.musicloud.models.Song;
import com.musicloud.models.User;
import com.musicloud.models.dtos.SongUploadDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.SongRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        String songUrl = storageService.saveSong(songUploadDto.getSongFile());
        song.setSongUrl(songUrl);
        String imageUrl = storageService.saveImage(songUploadDto.getImageFile());
        song.setImageUrl(imageUrl);
        User user = userService.getUserById(userDetails.getId());
        song.setCreator(user);

        songRepository.save(song);
    }
}
