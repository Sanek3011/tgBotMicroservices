package org.example.orderservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.kafka.events.OrderRequestedEvent;
import org.example.orderservice.service.ItemService;
import org.example.orderservice.service.OrderProcessService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRequestListener {

    private final OrderProcessService orderProcessService;

    @KafkaListener(topics = "order.request",
            properties = {
                    "spring.json.value.default.type=org.example.orderservice.kafka.events.OrderRequestedEvent",
            })
    public void orderRequestListener(OrderRequestedEvent event) {
        orderProcessService.orderProcessPrice(event);

    }
}
