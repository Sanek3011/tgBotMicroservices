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
import org.example.userservice.service.notification.NotificationRequest;
import org.example.userservice.service.notification.NotificationSender;
import org.example.userservice.util.NotificationRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final NotificationSender notificationService;
    private final PaymentFailedProducer failedProducer;
    private final PaymentSuccessProducer successProducer;


    @Transactional
    public void processPayment(OrderRequestedEvent event) {
        User user;
        if (event.getTgId() != null) {
            user = userRepository.findByTelegramId(event.getTgId()).orElseThrow();
        }else {
            user = userRepository.findById(event.getUserId()).orElseThrow();
        }

        PaymentEvent paymentEvent = new PaymentEvent(UUID.randomUUID(), user.getId(), event.getOrderId());
        if (user.getScore() < event.getPrice()) {
            notificationService.sendNotification(NotificationRequestFactory.notificationForTgId(event.getTgId(), "Недостаточно баллов"));
            failedProducer.sendPaymentEvent(paymentEvent);
            return;
        }
        user.setScore(user.getScore() - event.getPrice());
        userRepository.save(user);
        successProducer.sendPaymentEvent(paymentEvent);

    }
}
