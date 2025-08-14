package org.example.orderservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.kafka.events.OrderProcessEvent;
import org.example.orderservice.service.OrderProcessService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProcessListener {

    private final OrderProcessService orderProcessService;

    @KafkaListener(topics = "order.process",
            properties = {
                    "spring.json.value.default.type=org.example.orderservice.kafka.events.OrderProcessEvent",
            })
    public void listerOrderProcess(OrderProcessEvent event) {
        orderProcessService.orderProcessStatus(event);
    }
}
