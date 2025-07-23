package org.example.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.kafka.events.UserRegisteredEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public static final String TOPIC = "user.registration";


    public void sendRegistration (UserRegisteredEvent event) {
        kafkaTemplate.send(TOPIC, event.getTgId().toString(), event);
    }
}
