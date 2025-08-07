package org.example.orderservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.kafka.events.NotificationEvent;
import org.example.orderservice.kafka.events.OrderRequestedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProcessProducer {

    private final KafkaTemplate<String, OrderRequestedEvent> kafkaTemplate;
    public static final String TOPIC = "order.request.v2";

    public void sendOrderEvent (OrderRequestedEvent event) {
        kafkaTemplate.send(TOPIC, event.getTgId().toString(), event);
    }
}
