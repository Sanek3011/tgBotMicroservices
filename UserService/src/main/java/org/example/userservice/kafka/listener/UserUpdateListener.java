package org.example.userservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.UserUpdateEvent;
import org.example.userservice.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateListener {

    private final UserService service;

    @KafkaListener(topics = "user.update",
            properties = {
                    "spring.json.value.default.type=org.example.userservice.kafka.events.UserUpdateEvent",
            })
    public void updateUserListener(UserUpdateEvent event) {
        service.updateUser(event);
    }
}
