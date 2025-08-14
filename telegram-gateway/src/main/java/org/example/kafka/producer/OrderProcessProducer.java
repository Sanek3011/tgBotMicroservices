package org.example.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.kafka.events.OrderProcessEvent;
import org.example.kafka.events.OrderRequestedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProcessProducer {

    private final KafkaTemplate<String, OrderProcessEvent> kafkaTemplate;

    public static final String TOPIC = "order.process";

    public void sendProcessOrder(OrderProcessEvent event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
    }

}
