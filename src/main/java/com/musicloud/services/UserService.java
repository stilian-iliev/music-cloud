package com.musicloud.services;

import com.musicloud.models.Playlist;
import com.musicloud.models.Song;
import com.musicloud.models.User;
import com.musicloud.models.dtos.playlist.PlaylistDto;
import com.musicloud.models.dtos.song.SongDto;
import com.musicloud.models.dtos.user.BasicUserDto;
import com.musicloud.models.dtos.user.EditProfileDto;
import com.musicloud.models.dtos.user.UserProfileDto;
import com.musicloud.models.exceptions.PlaylistNotFoundException;
import com.musicloud.models.exceptions.SongNotFoundException;
import com.musicloud.models.exceptions.UserNotFoundException;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.PlaylistRepository;
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
    private final PlaylistRepository playlistRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, SongRepository songRepository, PlaylistRepository playlistRepository, StorageService storageService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersMatching(String keyword) {
        if (keyword == null) return getAllUsers();
        return userRepository.findAll(keyword);
    }

    public List<User> getAllUsersOrderedByRole(String keyword) {
        return getAllUsersMatching(keyword).stream().sorted(Comparator.comparing((User u) -> u.getRoles().size()).reversed()).collect(Collectors.toList());
    }

    public List<PlaylistDto> findFollowingPlaylists(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new).getFollowedPlaylists().stream().map(PlaylistDto::new).collect(Collectors.toList());
    }

    public List<BasicUserDto> findFollowingUsers(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new).getFollowedUsers().stream().map(BasicUserDto::new).collect(Collectors.toList());
    }

    public void followPlaylist(UUID userId, UUID playlistId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(PlaylistNotFoundException::new);
        user.followPlaylist(playlist);
        userRepository.save(user);
    }

    public void unfollowPlaylist(UUID userId, UUID playlistId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(PlaylistNotFoundException::new);
        user.unfollowPlaylist(playlist);
        userRepository.save(user);
    }

    public boolean isFollowed(UUID userId, UUID playlistId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.isFollowingPlaylist(playlistId);
    }

    public void followUser(UUID myId, UUID userId) {
        User me = userRepository.findById(myId).orElseThrow(UserNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        me.followUser(user);
        userRepository.save(me);
    }

    public void unfollowUser(UUID myId, UUID userId) {
        User me = userRepository.findById(myId).orElseThrow(UserNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        me.unfollowUser(user);
        userRepository.save(me);
    }

    public boolean isUserFollowed(UUID myId, UUID userId) {
        User me = userRepository.findById(myId).orElseThrow(UserNotFoundException::new);
        return me.isFollowingUser(userId);

    }
}
