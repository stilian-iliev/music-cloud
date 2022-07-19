package com.musicloud.services;

import com.musicloud.models.dtos.DashboardDto;
import com.musicloud.repositories.PlaylistRepository;
import com.musicloud.repositories.SongRepository;
import com.musicloud.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DashboardService {
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

    public DashboardService(UserRepository userRepository, SongRepository songRepository, PlaylistRepository playlistRepository) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
    }

    public DashboardDto getDashboardDto() {
        //todo: get new users
        return new DashboardDto(userRepository.count(), songRepository.count(), playlistRepository.count(), userRepository.countByCreationDateAfter(LocalDateTime.now().minusDays(1)));
    }
}
