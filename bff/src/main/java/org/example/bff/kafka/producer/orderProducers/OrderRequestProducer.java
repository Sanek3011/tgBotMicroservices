package org.example.bff.kafka.producer.orderProducers;

import org.example.bff.kafka.events.orderEvents.OrderRequestEvent;
import org.example.bff.kafka.producer.AbstractProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderRequestProducer extends AbstractProducer<OrderRequestEvent> {

    public static final String TOPIC = "order.request";

    public OrderRequestProducer(KafkaTemplate<String, OrderRequestEvent> kafkaTemplate) {
        super(kafkaTemplate, TOPIC);
    }

}
