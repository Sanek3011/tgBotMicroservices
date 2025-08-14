package org.example.orderservice.service.notification;

import lombok.RequiredArgsConstructor;

import org.example.orderservice.kafka.events.NotificationEvent;
import org.example.orderservice.kafka.producer.NotificationProducer;
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
                .userId(notificationRequest.getUserId())
                .tgId(notificationRequest.getTgId())
                .message(notificationRequest.getMessage())
                .roles(notificationRequest.getRoles())
                .build());
    }
}
