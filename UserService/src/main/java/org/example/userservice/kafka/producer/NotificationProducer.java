package org.example.userservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.UserServiceNotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, UserServiceNotificationEvent> kafkaTemplate;
    public static final String TOPIC = "notification";

    public void sendNotification (UserServiceNotificationEvent event) {
        kafkaTemplate.send(TOPIC, event.getTgId().toString(), event);
    }


}



