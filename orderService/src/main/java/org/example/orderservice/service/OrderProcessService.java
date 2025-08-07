package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.Status;
import org.example.orderservice.kafka.events.NotificationEvent;
import org.example.orderservice.kafka.events.OrderRequestedEvent;
import org.example.orderservice.kafka.events.PaymentEvent;
import org.example.orderservice.kafka.producer.NotificationProducer;
import org.example.orderservice.kafka.producer.OrderProcessProducer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderProcessService {

    private final OrderService orderService;
    private final ItemService service;
    private final OrderProcessProducer producer;
    private final NotificationProducer notificationProducer;


    public void orderProcessPrice(OrderRequestedEvent event) {
        Integer price = service.getItemByName(event.getItem()).getPrice();
        if (price == null) {
            notificationProducer.sendNotification(new NotificationEvent(UUID.randomUUID(),null, event.getTgId(), "Товар не найден. Попробуйте снова", null));
            return;
        }
        Long id = orderService.saveOrder(event).getId();
        event.setPrice(price);
        event.setOrderId(id);
        producer.sendOrderEvent(event);
    }


    public void orderProcessAccept(PaymentEvent event) {
        Order byId = orderService.findById(event.getOrderId());
        byId.setUserId(event.getUserId());
        byId.setStatus(Status.NEW);
        orderService.saveOrder(byId);
        notificationProducer.sendNotification(new NotificationEvent(UUID.randomUUID(),event.getUserId(), null, "Заказ успешно создан. Ожидайте обработки", null));
        notificationProducer.sendNotification(new NotificationEvent(UUID.randomUUID(),
                null,
                null,
                String.format("Поступил новый заказ от пользователя c ID: %d", event.getUserId()),
                List.of("ADMIN")));


    }

    public void orderProcessFailed(PaymentEvent event) {
        Order byId = orderService.findById(event.getOrderId());
        byId.setUserId(event.getUserId());
        byId.setStatus(Status.REJECTED);
        orderService.saveOrder(byId);
    }

}
