package com.musicloud.repositories;

import com.musicloud.models.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LikedRepository extends JpaRepository<Liked, UUID> {
}
