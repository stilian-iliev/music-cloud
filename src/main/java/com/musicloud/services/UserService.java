package com.musicloud.services;

import com.musicloud.models.BasePlaylist;
import com.musicloud.models.Playlist;
import com.musicloud.models.Song;
import com.musicloud.models.User;
import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.user.EditProfileDto;
import com.musicloud.models.dtos.user.UserProfileDto;
import com.musicloud.models.exceptions.SongNotFoundException;
import com.musicloud.models.exceptions.UserNotFoundException;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.SongRepository;
import com.musicloud.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, SongRepository songRepository, StorageService storageService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
    }

    public EditProfileDto getEditProfileDto(String email) {
        return userRepository.findProfileDtoOf(email);
    }

    public void editProfile(EditProfileDto editProfileDto, AppUserDetails userDetails) throws IOException {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotFoundException::new);

        if (!editProfileDto.getUsername().trim().isEmpty()) {
            user.setUsername(editProfileDto.getUsername());
            userDetails.setDisplayName(editProfileDto.getUsername());
        }

        user.setFirstName(editProfileDto.getFirstName());
        user.setLastName(editProfileDto.getLastName());
        if (!editProfileDto.getImage().isEmpty()) {
            String imageUrl = storageService.saveImage(editProfileDto.getImage(), "avatars");
            if (imageUrl != null) {
                user.setImageUrl(imageUrl);
                userDetails.setImageUrl(imageUrl);
            }
        }
        userRepository.save(user);

    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public UserProfileDto getProfileDto(UUID userId) {
        return userRepository.findById(userId).map(UserProfileDto::new).orElseThrow(UserNotFoundException::new);
    }

    public void likeSong(UUID songId, AppUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotFoundException::new);
        user.getLiked().addSong(songRepository.findById(songId).orElseThrow(SongNotFoundException::new));
        userRepository.save(user);
    }

    public void dislikeSong(UUID songId, AppUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotFoundException::new);
        user.getLiked().removeSong(songRepository.findById(songId).orElseThrow(SongNotFoundException::new));
        userRepository.save(user);
    }

    public PlaylistDto findLiked(UUID id) {
        return new PlaylistDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new).getLiked());
    }

    public List<PlaylistDto> findPlaylistsOfUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new).getPlaylists().stream()
                .sorted(Comparator.comparing(Playlist::getCreationTime))
                .map(PlaylistDto::new).collect(Collectors.toList());
    }

    public List<SongDto> getSongsByUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new).getSongs().stream()
                .sorted(Comparator.comparing(Song::getCreationTime))
                .map(SongDto::new).collect(Collectors.toList());
    }

    public void clearStorage() throws Exception {
        List<String> active = userRepository.findAll().stream()
                .map(User::getImageUrl).toList();
        storageService.deleteUnusedFilesFromFolder("avatars", active);
    }
}
