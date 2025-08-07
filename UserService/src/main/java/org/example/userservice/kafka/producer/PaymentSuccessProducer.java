package org.example.userservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.PaymentEvent;
import org.example.userservice.kafka.events.UserRoleEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentSuccessProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    public static final String TOPIC = "user.payment.success";

    public void sendPaymentEvent(PaymentEvent event) {
        kafkaTemplate.send(TOPIC, event.getId().toString(), event);
    }
}
