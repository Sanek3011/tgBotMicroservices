package org.example.userservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.PaymentEvent;
import org.example.userservice.service.PaymentService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFailedProducer implements PaymentProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    public static final String TOPIC = "user.payment.failed";

    public void sendPaymentEvent(PaymentEvent event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
    }
}
