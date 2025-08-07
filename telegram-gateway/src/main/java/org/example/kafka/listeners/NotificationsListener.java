package org.example.kafka.listeners;

import lombok.RequiredArgsConstructor;
import org.example.kafka.events.NotificationEvent;
import org.example.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationsListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "notification",
            properties = {
                    "spring.json.value.default.type=org.example.kafka.events.NotificationEvent",
            })
    public void listenNotification(NotificationEvent event) {
        notificationService.sendMessage(event);
    }
}
