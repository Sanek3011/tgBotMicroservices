package org.example.bff.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;

import java.util.UUID;

public abstract class AbstractProducer<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;
    private final String topic;

    public AbstractProducer(KafkaTemplate<String, T> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public Mono<Void> sendEvent(T event) {
         kafkaTemplate.send(topic, UUID.randomUUID().toString(), event);
        return Mono.empty();
    }
}
