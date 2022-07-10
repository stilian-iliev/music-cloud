package com.musicloud.repositories;

import com.musicloud.models.User;
import com.musicloud.models.dtos.user.EditProfileDto;
import com.musicloud.models.dtos.song.SongDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("select new com.musicloud.models.dtos.user.EditProfileDto(u.firstName, u.lastName, u.username) from User u where u.email = ?1")
    EditProfileDto findProfileDtoOf(String email);

    @Query("select new com.musicloud.models.dtos.song.SongDto(s) from User u join Song s where u.email = ?1")
    List<SongDto> getSongsFromUserWithId(String email);
}
