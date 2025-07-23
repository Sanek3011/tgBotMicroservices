package org.example.kafka.listeners;

import lombok.RequiredArgsConstructor;
import org.example.kafka.events.UserNotificationEvent;
import org.example.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationsListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "notification",
            properties = {
                    "spring.json.value.default.type=org.example.kafka.events.UserNotificationEvent",
            })
    public void listenNotification(UserNotificationEvent event) {
        notificationService.sendMessage(event);
    }
}
