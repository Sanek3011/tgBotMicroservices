package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.NotificationEvent;
import org.example.userservice.kafka.producer.NotificationProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationProducer producer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNotification(Long chatId, String message) {
        producer.sendNotification(new NotificationEvent(UUID.randomUUID() ,chatId, message));
    }
}
