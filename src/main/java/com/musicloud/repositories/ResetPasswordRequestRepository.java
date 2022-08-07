package com.musicloud.repositories;

import com.musicloud.models.ResetPasswordRequest;
import com.musicloud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, UUID> {
    List<ResetPasswordRequest> findAllByUser(User user);

    void deleteAllByUser(User user);

    ResetPasswordRequest findByUserId(UUID id);
}
