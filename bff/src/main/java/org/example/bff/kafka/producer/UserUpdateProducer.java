package org.example.bff.kafka.producer;

import org.example.bff.kafka.events.UserUpdateEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateProducer extends AbstractProducer<UserUpdateEvent> {

    public static final String TOPIC = "user.update";

    public UserUpdateProducer(KafkaTemplate<String, UserUpdateEvent> kafkaTemplate) {
        super(kafkaTemplate, TOPIC);
    }
}
