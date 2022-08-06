package com.musicloud.schedulers;

import com.musicloud.services.PlaylistService;
import com.musicloud.services.SongService;
import com.musicloud.services.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClearStorageScheduler {
    private final UserService userService;
    private final SongService songService;
    private final PlaylistService playlistService;

    public ClearStorageScheduler(UserService userService, SongService songService, PlaylistService service) {
        this.userService = userService;
        this.songService = songService;
        this.playlistService = service;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void cleanStorage() throws Exception {
        userService.clearStorage();
        songService.clearStorage();
        playlistService.clearStorage();
    }
}
