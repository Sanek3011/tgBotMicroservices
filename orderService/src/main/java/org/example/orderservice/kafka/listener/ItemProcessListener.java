package org.example.orderservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.kafka.events.ItemProcessEvent;
import org.example.orderservice.kafka.events.OrderProcessEvent;
import org.example.orderservice.service.ItemProcessService;
import org.example.orderservice.service.OrderProcessService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemProcessListener {

    private final ItemProcessService itemProcessService;

    @KafkaListener(topics = "item.process",
            properties = {
                    "spring.json.value.default.type=org.example.orderservice.kafka.events.ItemProcessEvent",
            })
    public void listerOrderProcess(ItemProcessEvent event) {
        itemProcessService.processItem(event);
    }
}
