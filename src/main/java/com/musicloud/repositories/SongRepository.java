package com.musicloud.repositories;

import com.musicloud.models.Song;
import com.musicloud.models.dtos.song.SongDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select s from Song s where concat_ws(' ', s.title, s.creator.username) like %?1%")
    Page<Song> findAllByMatching(String query, Pageable pageable);
}
