package org.example.handler.orderHandlers;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;
import org.example.kafka.events.OrderRequestedEvent;
import org.example.kafka.producer.CreateOrderProducer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler implements CommandHandler {

    private final CreateOrderProducer orderProducer;

    @Override
    public String getCommandName() {
        return "createOrder";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (user.getRole().equals(Role.GUEST)) {
            return List.of(new ReplyMessage(user.getTgId(), "НЕТ ДОСТУПА.", null));
        }
        String item = update.substring(6);
        OrderRequestedEvent event = new OrderRequestedEvent(UUID.randomUUID(), item, user.getTgId());
        orderProducer.sendRequestOrder(event);
        return List.of(new ReplyMessage(user.getTgId(), "Запрос обрабатывается", null));
    }
}
