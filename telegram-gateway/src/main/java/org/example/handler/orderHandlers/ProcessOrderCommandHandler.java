package org.example.handler.orderHandlers;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;
import org.example.kafka.events.OrderProcessEvent;
import org.example.kafka.producer.OrderProcessProducer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcessOrderCommandHandler implements CommandHandler {

    private final OrderProcessProducer producer;

    @Override
    public String getCommandName() {
        return "processOrder";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            return List.of(new ReplyMessage(user.getTgId(), "НЕТ ДОСТУПА.", null));
        }
        String[] tmp = update.toUpperCase().split("_");
        if (tmp.length != 3) {
            return List.of(new ReplyMessage(user.getTgId(), "внутрення ошибка", null));
        }
        OrderProcessEvent event = OrderProcessEvent.builder()
                .id(UUID.randomUUID())
                .orderId(Long.parseLong(tmp[2]))
                .status(tmp[1])
                .initiatorId(user.getTgId())
                .build();
        producer.sendProcessOrder(event);
        return List.of();
    }
}
