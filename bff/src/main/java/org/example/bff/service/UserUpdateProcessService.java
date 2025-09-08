package org.example.bff.service;

import lombok.RequiredArgsConstructor;
import org.example.bff.dto.UserDto;
import org.example.bff.kafka.events.UserUpdateEvent;
import org.example.bff.kafka.producer.UserUpdateProducer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpdateProcessService {

    private final UserUpdateProducer producer;

    public void prepareUserEvent(Long id, String name, String telegramId, String role, String score) {
        UserUpdateEvent event = UserUpdateEvent.builder()
                .id(id)
                .name(name)
                .tgId(Long.parseLong(telegramId))
                .role(role)
                .score(Integer.parseInt(score))
                .isWeb(true)
                .build();
        producer.sendEvent(event);
    }
}
