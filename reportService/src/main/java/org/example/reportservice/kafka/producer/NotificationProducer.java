package org.example.reportservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.kafka.event.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    public static final String TOPIC = "notification";

    public void sendNotification (NotificationEvent event) {
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(), event);
    }


}



