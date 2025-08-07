package org.example.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.kafka.events.OrderRequestedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderProducer {

    private final KafkaTemplate<String, OrderRequestedEvent> kafkaTemplate;

    public static final String TOPIC = "order.request";

    public void sendRequestOrder(OrderRequestedEvent event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
    }
}
