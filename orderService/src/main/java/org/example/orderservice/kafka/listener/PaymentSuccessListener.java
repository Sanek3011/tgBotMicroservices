package org.example.orderservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.kafka.events.PaymentEvent;
import org.example.orderservice.service.OrderProcessService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PaymentSuccessListener {

    private final OrderProcessService orderService;

    @KafkaListener(topics = "user.payment.success",
            properties = {
                    "spring.json.value.default.type=org.example.orderservice.kafka.events.PaymentEvent",
            })
    public void acceptOrder(PaymentEvent event) {
        orderService.orderPaymentAccept(event);
    }
}
