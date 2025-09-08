package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.Jti;
import org.example.userservice.repository.JtiRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CleanUpService {

    private final JtiRepository jtiRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanUp() {
        jtiRepository.removeByExpiresAtBefore(Instant.now());
    }
}
