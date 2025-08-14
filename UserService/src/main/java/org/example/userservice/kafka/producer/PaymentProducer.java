package org.example.userservice.kafka.producer;

import org.example.userservice.kafka.events.PaymentEvent;

public interface PaymentProducer {

    void sendPaymentEvent(PaymentEvent paymentEvent);
}
