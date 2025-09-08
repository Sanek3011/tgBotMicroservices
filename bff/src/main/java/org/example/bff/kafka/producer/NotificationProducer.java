package org.example.bff.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.bff.kafka.events.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component

public class NotificationProducer extends AbstractProducer<NotificationEvent> {

    public static final String TOPIC = "notification";

    public NotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        super(kafkaTemplate, TOPIC);
    }

}
