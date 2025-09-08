package org.example.userservice.repository;

import org.example.userservice.entity.Role;
import org.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(Long telegramId);

    List<User> findByRoleNotIn(Collection<Role> roles);

    Optional<User> findByName(String name);

    Optional<User> findByToken(String token);
}
