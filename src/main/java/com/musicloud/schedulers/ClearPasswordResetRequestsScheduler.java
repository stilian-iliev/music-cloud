package com.musicloud.schedulers;

import com.musicloud.models.ResetPasswordRequest;
import com.musicloud.repositories.ResetPasswordRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class ClearPasswordResetRequestsScheduler {
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;

    public ClearPasswordResetRequestsScheduler(ResetPasswordRequestRepository resetPasswordRequestRepository) {
        this.resetPasswordRequestRepository = resetPasswordRequestRepository;
    }

    @Scheduled(cron = "0 0 0,12 * * ?")
    public void cleanRepo() {
        List<ResetPasswordRequest> all = resetPasswordRequestRepository.findAll();
        List<ResetPasswordRequest> expired = all.stream().filter(ResetPasswordRequest::isExpired).toList();
        resetPasswordRequestRepository.deleteAll(expired);
    }
}
