package org.example.userservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.OrderRequestedEvent;
import org.example.userservice.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRequestListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "order.request.v2",
            properties = {
                    "spring.json.value.default.type=org.example.userservice.kafka.events.OrderRequestedEvent",
            })
    public void processPayment(OrderRequestedEvent event) {
        paymentService.processPayment(event);
    }
}
