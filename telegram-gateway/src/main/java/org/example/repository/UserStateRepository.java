package org.example.repository;

import org.example.entity.Role;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStateRepository extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findByTgId(Long tgId);


    @Modifying
    @Query("UPDATE TelegramUser set state = :state where tgId = :chatId")
    void updateUserState(@Param("chatId") Long chatId, @Param("state") State state);

    TelegramUser findByUserId(Long userId);

    @Query("SELECT tgId from TelegramUser where role in :roles")
    List<Long> findAllIdByRole(@Param("roles") List<Role> roles);
}
