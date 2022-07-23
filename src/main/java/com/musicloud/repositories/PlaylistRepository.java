package com.musicloud.repositories;

import com.musicloud.models.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {
    List<Playlist> findAllByUserId(UUID userId);

    List<Playlist> findAllByUserIdOrderByCreationTime(UUID userId);

    @Query("select p from Playlist p where concat_ws(' ', p.name) like %?1%")
    List<Playlist> findAll(String query);
}
