package org.example.reportservice.service.notification.notification;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.kafka.event.NotificationEvent;
import org.example.reportservice.kafka.producer.NotificationProducer;
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
                .userId(notificationRequest.getUserId())
                .message(notificationRequest.getMessage())
                .build());
    }
}
