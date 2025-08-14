package org.example.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.Item;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.Status;
import org.example.orderservice.kafka.events.OrderProcessEvent;
import org.example.orderservice.kafka.events.OrderRequestedEvent;
import org.example.orderservice.kafka.events.PaymentEvent;
import org.example.orderservice.kafka.producer.OrderProcessProducer;
import org.example.orderservice.service.notification.NotificationSender;
import org.example.orderservice.util.NotificationRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProcessService {

    private final OrderService orderService;
    private final ItemService itemService;
    private final OrderProcessProducer producer;
    private final NotificationSender notificationSender;


    public void orderProcessPrice(OrderRequestedEvent event) {
        Item item = itemService.getItemByName(event.getItem());
        if (item == null) {
            notificationSender.sendNotification(NotificationRequestFactory.notificationForTgId(event.getTgId(), "Товар не найден"));
            return;
        }
        Long id = orderService.saveOrder(event).getId();
        event.setPrice(item.getPrice());
        event.setOrderId(id);
        producer.sendOrderEvent(event);
    }

    @Transactional
    public void orderPaymentAccept(PaymentEvent event) {
        Order byId = orderService.findById(event.getOrderId());
        byId.setUserId(event.getUserId());
        byId.setStatus(Status.NEW);
        Long id = orderService.saveOrder(byId);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                notificationSender.sendNotification(NotificationRequestFactory.notificationForUserId(byId.getUserId(), String.format("Заказ успешно создан ID: %d. Ожидайте обработки", id)));
                notificationSender.sendNotification(NotificationRequestFactory.notificationForRoles(
                        String.format("Поступил новый заказ от пользователя c ID: %d",
                                event.getUserId()),
                        List.of("ADMIN")));
            }
        });


    }

    @Transactional
    public void orderPaymentFailed(PaymentEvent event) {
        Order byId = orderService.findById(event.getOrderId());
        byId.setUserId(event.getUserId());
        byId.setStatus(Status.REJECTED);
        orderService.saveOrder(byId);
    }

    @Transactional
    public void orderProcessStatus (OrderProcessEvent event) {
        Order order = orderService.findById(event.getOrderId());
        order.setStatus(Status.valueOf(event.getStatus()));
        orderService.saveOrder(order);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                notificationSender.sendNotification(NotificationRequestFactory.notificationForUserId(order.getUserId(), String.format("Статус вашего заказа ID: %d изменен на %s", order.getId(), order.getStatus())));
                notificationSender.sendNotification(NotificationRequestFactory.notificationForTgId(event.getInitiatorId(), String.format("Статус заказа успешно изменен на %s", order.getStatus())));
            }
        });

    }

}
