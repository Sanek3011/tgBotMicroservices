package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "telegram_users")
public class TelegramUser {
    @Id
    @Column(name = "tg_id")
    private Long tgId;
    @Column(name = "user_id")
    private Long userId;
    @Enumerated(EnumType.STRING)
    private State state;
    @Enumerated(EnumType.STRING)
    private Role role;
}
