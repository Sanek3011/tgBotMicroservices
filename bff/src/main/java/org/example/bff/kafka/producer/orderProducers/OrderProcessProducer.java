package org.example.bff.kafka.producer.orderProducers;

import org.example.bff.kafka.events.orderEvents.OrderProcessEvent;
import org.example.bff.kafka.producer.AbstractProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessProducer extends AbstractProducer<OrderProcessEvent> {

    public static final String TOPIC = "order.process";

    public OrderProcessProducer(KafkaTemplate<String, OrderProcessEvent> kafkaTemplate) {
        super(kafkaTemplate, TOPIC);
    }

}
