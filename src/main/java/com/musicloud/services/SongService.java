package com.musicloud.services;

import com.musicloud.models.dtos.SongUploadDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.SongRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final StorageService storageService;

    public SongService(SongRepository songRepository, StorageService storageService) {
        this.songRepository = songRepository;
        this.storageService = storageService;
    }


    public void add(SongUploadDto songUploadDto, AppUserDetails userDetails) throws IOException {
        String songUrl = storageService.saveSong(songUploadDto.getSongFile());
        String imageUrl = storageService.saveImage(songUploadDto.getImageFile());
    }
}
