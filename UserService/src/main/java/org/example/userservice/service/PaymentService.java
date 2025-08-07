package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.User;
import org.example.userservice.kafka.events.NotificationEvent;
import org.example.userservice.kafka.events.OrderRequestedEvent;
import org.example.userservice.kafka.events.PaymentEvent;
import org.example.userservice.kafka.producer.NotificationProducer;
import org.example.userservice.kafka.producer.PaymentFailedProducer;
import org.example.userservice.kafka.producer.PaymentSuccessProducer;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;
    private final PaymentFailedProducer failedProducer;
    private final PaymentSuccessProducer successProducer;


    @Transactional
    public void processPayment(OrderRequestedEvent event) {
        User user = userRepository.findByTelegramId(event.getTgId()).orElseThrow();
        PaymentEvent paymentEvent = new PaymentEvent(UUID.randomUUID(), user.getId(), event.getOrderId());
        if (user.getScore() < event.getPrice()) {
            notificationProducer.sendNotification(new NotificationEvent(UUID.randomUUID(), event.getTgId(), "Недостаточно баллов"));
            failedProducer.sendPaymentEvent(paymentEvent);
            return;
        }
        user.setScore(user.getScore() - event.getPrice());
        userRepository.save(user);
        successProducer.sendPaymentEvent(paymentEvent);

    }
}
