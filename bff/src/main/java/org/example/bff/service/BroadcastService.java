package org.example.bff.service;

import lombok.RequiredArgsConstructor;
import org.example.bff.kafka.events.NotificationEvent;
import org.example.bff.kafka.producer.NotificationProducer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BroadcastService {

    private final NotificationProducer producer;


    public void prepareBroadcastEvent(List<String> roles, String message, String telegramId) {
        NotificationEvent event = NotificationEvent.builder()
                        .message(message).build();
        if (roles != null && !roles.isEmpty()) {
            event.setRoles(roles);
        }
        if (telegramId != null && !telegramId.isEmpty()) {
            event.setTgId(Long.parseLong(telegramId));
        }
        producer.sendEvent(event);
    }
}
