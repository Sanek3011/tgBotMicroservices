package org.example.repository;

import org.example.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStateRepository extends JpaRepository<TelegramUser, Long> {
}
