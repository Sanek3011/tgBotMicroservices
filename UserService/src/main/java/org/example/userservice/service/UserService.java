package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.Role;
import org.example.userservice.entity.User;
import org.example.userservice.kafka.events.UserRegisteredEvent;
import org.example.userservice.kafka.events.UserRoleEvent;
import org.example.userservice.kafka.events.UserServiceNotificationEvent;
import org.example.userservice.kafka.events.UserUpdateEvent;
import org.example.userservice.kafka.producer.NotificationProducer;
import org.example.userservice.kafka.producer.UserUpdateRoleProducer;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;
    private final UserUpdateRoleProducer userUpdateRoleProducer;

    @Transactional
    public void saveUser(UserRegisteredEvent userRegisteredEvent) {
        if (userRepository.findByTelegramId(userRegisteredEvent.getTgId()).orElse(null) == null) {
            User user = new User();
            user.setTelegramId(userRegisteredEvent.getTgId());
            user.setRole(Role.GUEST);
            user.setScore(0);
            userRepository.save(user);
            notificationProducer.sendNotification(new UserServiceNotificationEvent(userRegisteredEvent.getTgId(), "Успешно зарегистрирован"));

        }
    }

    @Transactional
    public void updateUser(UserUpdateEvent userUpdateEvent) {
        User user = userRepository.findByTelegramId(userUpdateEvent.getTgId()).orElseThrow();
        UserServiceNotificationEvent notification = new UserServiceNotificationEvent();
        notification.setTgId(userUpdateEvent.getTgId());
        StringBuilder sb = new StringBuilder();
        if (userUpdateEvent.getRole() != null) {
            user.setRole(Role.valueOf(userUpdateEvent.getRole()));
            sb.append(String.format("Ваша роль изменена на %s\n", userUpdateEvent.getRole()));
            userUpdateRoleProducer.sendNotification(new UserRoleEvent(user.getTelegramId(), user.getRole().toString()));
        }
        if (userUpdateEvent.getName() != null) {
            user.setName(userUpdateEvent.getName());
            sb.append(String.format("Ваш ник изменен на %s\n", userUpdateEvent.getName()));
        }
        if (userUpdateEvent.getScore() != null) {
            user.setScore(userUpdateEvent.getScore());
            sb.append(String.format("Ваши баллы изменены на %d\n", userUpdateEvent.getScore()));
        }
        userRepository.save(user);
        notification.setMessage(sb.toString());
        notificationProducer.sendNotification(notification);
    }

    public List<UserRoleEvent> getNonGuestUsers() {
        List<User> resultList = userRepository.findByRoleNotIn(List.of(Role.GUEST));
        return resultList.stream()
                .map(u -> new UserRoleEvent(u.getTelegramId(), u.getRole().toString()))
                .toList();
    }
}
