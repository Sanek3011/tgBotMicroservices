package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

@Entity
@Table(name = "jti")
public class Jti {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "expires_at")
    private Instant expiresAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "revoked")
    private Boolean revoked;
}
