package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.Role;
import org.example.userservice.entity.User;
import org.example.userservice.kafka.events.UserRegisteredEvent;
import org.example.userservice.kafka.events.UserRoleEvent;
import org.example.userservice.kafka.events.UserUpdateEvent;
import org.example.userservice.kafka.producer.UserUpdateRoleProducer;
import org.example.userservice.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final UserUpdateRoleProducer userUpdateRoleProducer;

    @Transactional
    public void saveUser(UserRegisteredEvent userRegisteredEvent) {
        if (userRepository.findByTelegramId(userRegisteredEvent.getTgId()).orElse(null) == null) {
            User user = new User();
            user.setTelegramId(userRegisteredEvent.getTgId());
            user.setRole(Role.GUEST);
            user.setScore(0);
            userRepository.save(user);
            notificationService.sendNotification(userRegisteredEvent.getTgId(), "Успешно зарегистрирован");
        }
    }

    public Map<Long, String> getAllNamesByIds(Set<Long> ids) {
        return userRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(User::getId, User::getName));
    }

    public User findUserByIdentification(UserUpdateEvent event) {
        if (event.getId() != null) {
            return userRepository.findById(event.getId()).orElse(null);
        }
        if (event.getTgId() != null) {
            return userRepository.findByTelegramId(event.getTgId()).orElse(null);
        }
        if (event.getName() != null) {
            return userRepository.findByName(event.getName()).orElse(null);
        }
        return null;
    }


    public void updateUser(UserUpdateEvent userUpdateEvent) {
        try {
        User user = findUserByIdentification(userUpdateEvent);
        StringBuilder sb = new StringBuilder();
        if (userUpdateEvent.getRole() != null) {
            user.setRole(Role.valueOf(userUpdateEvent.getRole().toUpperCase()));
            sb.append(String.format("Ваша роль изменена на %s\n", user.getRole()));
            userUpdateRoleProducer.sendNotification(new UserRoleEvent(UUID.randomUUID() ,user.getId(), user.getTelegramId(), user.getRole().toString()));
        }
        if (userUpdateEvent.getName() != null && !userUpdateEvent.getName().equals(user.getName())) {
            user.setName(userUpdateEvent.getName());
            sb.append(String.format("Ваш ник изменен на %s\n", user.getName()));
        }
        if (userUpdateEvent.getScore() != null) {
            user.setScore(user.getScore() + userUpdateEvent.getScore());
            sb.append(String.format("Баланс изменен. Новый баланс: %d баллов.\n", user.getScore()));
        }

            userRepository.saveAndFlush(user);
            notificationService.sendNotification(user.getTelegramId(), sb.toString());
        }catch (DataIntegrityViolationException e) {
            System.out.println(e.getClass());
            notificationService.sendNotification(userUpdateEvent.getInitiatorTgId(), "Имя занято. Выберите другое");
            return;
        }catch (IllegalArgumentException e) {
            notificationService.sendNotification(userUpdateEvent.getInitiatorTgId(), "Некорректная роль. Попробуйте снова");
        }
    }

    public List<UserRoleEvent> getNonGuestUsers() {
        List<User> resultList = userRepository.findByRoleNotIn(List.of(Role.GUEST));
        return resultList.stream()
                .map(u -> new UserRoleEvent(UUID.randomUUID(), u.getId(), u.getTelegramId(), u.getRole().toString()))
                .toList();
    }

    public Optional<Integer> getBalance(Long chatId) {
        return userRepository.findByTelegramId(chatId)
                .map(User::getScore);
    }

    public Integer getBalanceByName(String name) {
        User user = userRepository.findByName(name).orElse(null);
        return user == null ? -100 : user.getScore();
    }
}
