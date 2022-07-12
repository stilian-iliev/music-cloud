package com.musicloud.repositories;

import com.musicloud.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SongRepository extends JpaRepository<Song, UUID> {
    List<Song> findAllByCreatorId(UUID id);

    Optional<Song> findByIdOrderByCreationTime(UUID songId);

    List<Song> findAllByCreatorIdOrderByCreationTime(UUID id);
}
