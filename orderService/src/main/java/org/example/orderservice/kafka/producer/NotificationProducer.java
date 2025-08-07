package org.example.orderservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.kafka.events.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    public static final String TOPIC = "notification";

    public void sendNotification (NotificationEvent event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
    }


}
