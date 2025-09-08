package org.example.userservice.repository;

import org.example.userservice.entity.Jti;
import org.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface JtiRepository extends JpaRepository<Jti, String> {
    List<Jti> findByExpiresAtBefore(Instant expiresAtBefore);

    void removeByExpiresAtBefore(Instant expiresAtBefore);

    List<Jti> findByUser(User user);

    @Modifying
    @Query("update Jti j set j.revoked = :revoked where j.user = :user")
    void updateRevoke(@Param("user") User user, @Param("revoked") Boolean revoked);
}
