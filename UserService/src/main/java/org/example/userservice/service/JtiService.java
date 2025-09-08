package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.Jti;
import org.example.userservice.entity.User;
import org.example.userservice.repository.JtiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JtiService {

    private final JtiRepository repository;

    public void save(String id, User user, Instant expiresAt) {
        Jti jti = new Jti(id, expiresAt, user, false);
        repository.save(jti);
    }

    public boolean validateJti(String jti) {
        return repository.findById(jti)
                .filter(t -> !t.getRevoked())
                .filter(t -> t.getExpiresAt().isAfter(Instant.now()))
                .isPresent();
    }

    @Transactional
    public void removeOldToken(User user) {
        repository.updateRevoke(user, true);
    }

}
