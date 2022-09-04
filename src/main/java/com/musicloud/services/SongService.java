package com.musicloud.services;

import com.musicloud.models.*;
import com.musicloud.models.dtos.song.EditSongDto;
import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.song.SongUploadDto;
import com.musicloud.models.exceptions.SongNotFoundException;
import com.musicloud.models.exceptions.UnauthorizedException;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.SongRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Map<String, String > songInfo = storageService.saveSong(songUploadDto.getSongFile());
        song.setSongUrl(songInfo.get("url"));
        song.setDuration(Integer.parseInt(songInfo.get("duration")));
        User user = userService.getUserById(userDetails.getId());
        song.setCreator(user);

        songRepository.save(song);
    }

    public SongDto getSongById(UUID songId) {
        return songRepository.findByIdOrderByCreationTime(songId).map(SongDto::new).orElseThrow(SongNotFoundException::new);
    }

    public Page<SongDto> getAllSongs(Pageable pageable) {
        return songRepository.findAll(pageable).map(SongDto::new);
    }

    public void editSong(UUID songId, EditSongDto songDto, AppUserDetails userDetails) {
        Song song = songRepository.findById(songId).orElseThrow(SongNotFoundException::new);
        if (!song.getCreator().getId().equals(userDetails.getId())) throw new UnauthorizedException();
        song.setTitle(songDto.getTitle());
        songRepository.save(song);
    }

    public void deleteSong(UUID songId, AppUserDetails userDetails) {
        deleteSong(songId, userDetails.getId());

    }
    public void deleteSong(UUID songId, UUID userId) {
        Song song = songRepository.findById(songId).orElseThrow(SongNotFoundException::new);
        if (!song.getCreator().getId().equals(userId)) throw new UnauthorizedException();
        for (Liked playlist : song.getLikedPlaylists()) {
            playlist.removeSong(song);
        }
        for (Playlist playlist : song.getPlaylists()) {
            playlist.removeSong(song);
        }
        songRepository.delete(song);

    }

    public void clearStorage() throws Exception {
        List<String> active = songRepository.findAll().stream()
                .map(Song::getSongUrl).toList();
        storageService.deleteUnusedFilesFromFolder("songs", active);
    }

    public Page<SongDto> getAllSongs(String query, Pageable pageable) {
        //test
        if (query == null) return getAllSongs(pageable);
        return songRepository.findAllByMatching(query, pageable).map(SongDto::new);
    }
}
