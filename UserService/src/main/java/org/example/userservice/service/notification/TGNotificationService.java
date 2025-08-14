package org.example.userservice.service.notification;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.NotificationEvent;
import org.example.userservice.kafka.producer.NotificationProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TGNotificationService implements NotificationSender {

    private final NotificationProducer producer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        producer.sendNotification(NotificationEvent
                .builder()
                .id(UUID.randomUUID())
                .tgId(notificationRequest.getTgId())
                .message(notificationRequest.getMessage())
                .build());
    }
}
