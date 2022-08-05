package com.musicloud.repositories;

import com.musicloud.models.ResetPasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, UUID> {
}
