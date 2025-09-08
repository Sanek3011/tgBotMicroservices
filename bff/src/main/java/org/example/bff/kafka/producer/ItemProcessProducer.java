package org.example.bff.kafka.producer;

import org.example.bff.kafka.events.orderEvents.ItemProcessEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ItemProcessProducer extends AbstractProducer<ItemProcessEvent>{

    public static final String TOPIC = "item.process";

    public ItemProcessProducer(KafkaTemplate<String, ItemProcessEvent> kafkaTemplate) {
        super(kafkaTemplate, TOPIC);
    }
}
