package org.example.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.kafka.events.UserUpdateEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserProducer {

    private final KafkaTemplate<String, UserUpdateEvent> kafkaTemplate;

    public static final String TOPIC = "user.update";

    public void sendUpdate(UserUpdateEvent event) {
        event.setIsWeb(false);
        kafkaTemplate.send(TOPIC, event.getEventId().toString(), event);
    }
}
