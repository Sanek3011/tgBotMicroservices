package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserDto;
import org.example.userservice.dto.UserTgVisualDto;
import org.example.userservice.entity.Role;
import org.example.userservice.entity.User;
import org.example.userservice.kafka.events.UserRegisteredEvent;
import org.example.userservice.kafka.events.UserRoleEvent;
import org.example.userservice.kafka.events.UserUpdateEvent;
import org.example.userservice.kafka.producer.UserUpdateRoleProducer;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.service.notification.NotificationSender;
import org.example.userservice.util.NotificationRequestFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationSender notificationService;
    private final UserUpdateRoleProducer userUpdateRoleProducer;

    @Transactional
    public void saveUser(UserRegisteredEvent userRegisteredEvent) {
        if (userRepository.findByTelegramId(userRegisteredEvent.getTgId()).isEmpty()) {
            User user = new User();
            user.setTelegramId(userRegisteredEvent.getTgId());
            user.setRole(Role.GUEST);
            user.setScore(0);
            userRepository.save(user);
            notificationService.sendNotification(NotificationRequestFactory.notificationForTgId(user.getTelegramId(), "Успешно зарегистрирован"));
        }
    }

    public List<UserDto> getAllUsers() {
        List<User> all = userRepository.findAll(Sort.by("id"));
        return UserDto.toDtoList(all);
    }

    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }

    public Optional<User> findUserByTgId(Long tgId) {
        return userRepository.findByTelegramId(tgId);
    }

    public String generateOneTimeToken(User user) {
        String token = user.getToken();
        if (token == null || user.getExpiresAt().isBefore(LocalDateTime.now())) {
            token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setExpiresAt(LocalDateTime.now().plusMinutes(10));
            userRepository.save(user);
        }
        return token;
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
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

    public Set<String> getAllRoles() {
        return Role.getAllRoles();
    }

    public User findByUserId(Long id) {
        return userRepository.findById(id).orElseThrow();
    }


    public void updateUser(UserUpdateEvent userUpdateEvent) {
        List<String> replyList = new ArrayList<>();
        try {
        User user = findUserByIdentification(userUpdateEvent);
        if (StringUtils.hasText(userUpdateEvent.getRole()) && !user.getRole().toString().equals(userUpdateEvent.getRole())) {
            user.setRole(Role.valueOf(userUpdateEvent.getRole().toUpperCase()));
            replyList.add(String.format("Ваша роль изменена на %s", user.getRole()));
            userUpdateRoleProducer.sendNotification(new UserRoleEvent(UUID.randomUUID() ,user.getId(), user.getTelegramId(), user.getRole().toString()));
        }
        if (StringUtils.hasText(userUpdateEvent.getName()) && !userUpdateEvent.getName().equals(user.getName())) {
            user.setName(userUpdateEvent.getName());
            replyList.add(String.format("Ваш ник изменен на %s", user.getName()));
        }
        if (userUpdateEvent.getScore() != null) {
            if (userUpdateEvent.getIsWeb() && !Objects.equals(user.getScore(), userUpdateEvent.getScore())) {
                user.setScore(Math.max(userUpdateEvent.getScore(), 0));
                replyList.add(String.format("Баланс изменен. Новый баланс: %d баллов.", user.getScore()));
            }else if (!userUpdateEvent.getIsWeb()){
                user.setScore(user.getScore() + userUpdateEvent.getScore());
                replyList.add(String.format("Баланс изменен. Новый баланс: %d баллов.", user.getScore()));
            }
        }
        userRepository.saveAndFlush(user);
        String replyMessage = String.join("\n", replyList);
        notificationService.sendNotification(NotificationRequestFactory.notificationFull(userUpdateEvent.getId(), user.getTelegramId(), replyMessage));
        }catch (DataIntegrityViolationException e) {
            System.out.println(e.getClass());
            notificationService.sendNotification(NotificationRequestFactory.notificationFull(userUpdateEvent.getId(), userUpdateEvent.getTgId(), "Имя занято. Выберите другое"));
        }catch (IllegalArgumentException e) {
            notificationService.sendNotification(NotificationRequestFactory.notificationFull(userUpdateEvent.getId(), userUpdateEvent.getTgId(), "Некорректная роль"));
        }
    }

    public List<UserRoleEvent> getNonGuestUsers() {
        List<User> resultList = userRepository.findByRoleNotIn(List.of(Role.GUEST));
        return resultList.stream()
                .map(u -> new UserRoleEvent(UUID.randomUUID(), u.getId(), u.getTelegramId(), u.getRole().toString()))
                .toList();
    }

    public Optional<Integer> getBalance(Long userId) {
        return userRepository.findById(userId)
                .map(User::getScore);
    }

    public Integer getBalanceByName(String name) {
        User user = userRepository.findByName(name).orElse(null);
        return user == null ? null : user.getScore();
    }

    public List<UserTgVisualDto> getAllUsersForTg() {
        List<User> users = userRepository.findByRoleNotIn(Collections.singleton(Role.GUEST));
        return UserTgVisualDto.toDtoList(users);
    }

    public Long getCountUsers() {
        return userRepository.count();
    }
}
