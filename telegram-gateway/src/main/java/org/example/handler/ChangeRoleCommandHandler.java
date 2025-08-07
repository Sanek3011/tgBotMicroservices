package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.example.kafka.events.UserUpdateEvent;
import org.example.kafka.producer.UpdateUserProducer;
import org.example.service.TelegramBaseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChangeRoleCommandHandler implements CommandHandler {

    private final UpdateUserProducer producer;
    private final TelegramBaseService service;

    @Override
    public String getCommandName() {
        return "changeRole";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        Long chatId = user.getTgId();
        String[] split = update.split(" ");
        if (split.length != 2) {
            service.updateUserState(chatId, State.WAITING_CHANGEROLE);
            return List.of(new ReplyMessage(chatId, "Введите имя и роль. Пример: Nick_Name USER. Доступные роли: GUEST, USER. Для выхода введите /quit", null));
        }
        producer.sendUpdate(UserUpdateEvent
                .builder()
                .initiatorTgId(user.getTgId())
                .eventId(UUID.randomUUID())
                .role(split[1])
                .name(split[0]).build());
        service.updateUserState(chatId, State.NO);
        return List.of(new ReplyMessage(chatId, "Запрос обрабатывается...", null));
    }
}
