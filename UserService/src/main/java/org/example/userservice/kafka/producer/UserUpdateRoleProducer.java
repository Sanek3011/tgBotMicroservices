package org.example.userservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.UserRoleEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateRoleProducer {

    private final KafkaTemplate<String, UserRoleEvent> kafkaTemplate;
    public static final String TOPIC = "user.update.role";

    public void sendNotification (UserRoleEvent event) {
        kafkaTemplate.send(TOPIC, event.getTgId().toString(), event);
    }
}
