package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.example.kafka.events.UserUpdateEvent;
import org.example.kafka.producer.UpdateUserProducer;
import org.example.service.TelegramBaseService;
import org.example.service.output.UserOutputService;
import org.example.tempStorage.StringTempStorage;
import org.example.tempStorage.TempStorage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ModifyScoreCommandHandler implements CommandHandler {

    private final TelegramBaseService service;
    private final TempStorage<String> tempStorage;
    private final UserOutputService userOutputService;
    private final UpdateUserProducer updateUserProducer;


    @Override
    public String getCommandName() {
        return "modifyScore";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            return List.of(new ReplyMessage(user.getTgId(), "Нет доступа", null));
        }
        switch (user.getState()) {
            case NO:
                service.updateUserState(user.getTgId(), State.WAITING_NICKSCORE);
                return List.of(new ReplyMessage(user.getTgId(), "Введите ник, кому Вы хотите изменить очки", null));
            case WAITING_NICKSCORE:
                Optional<Integer> scoreFromUserService = userOutputService.getScoreFromUserService(update, true);
                if (scoreFromUserService.isEmpty()) {
                    return List.of(new ReplyMessage(user.getTgId(), "Пользователь не найден. Попробуйте снова или введите /quit", null));
                }
                tempStorage.put(user.getTgId(), update);
                service.updateUserState(user.getTgId(), State.WAITING_SCOREKEYBOARD);
                return List.of(new ReplyMessage(user.getTgId(), String.format("Текущее кол-во баллов: %d. Введите действие (пример +5 или -2)", scoreFromUserService.get()), null));
            case WAITING_SCOREKEYBOARD:
                try {
                    Integer action = Integer.parseInt(update);
                    UserUpdateEvent event = UserUpdateEvent.builder()
                            .initiatorTgId(user.getTgId())
                            .eventId(UUID.randomUUID())
                            .name(tempStorage.get(user.getTgId()).get())
                            .score(action)
                            .build();
                    updateUserProducer.sendUpdate(event);
                    service.updateUserState(user.getTgId(), State.NO);
                    tempStorage.remove(user.getTgId());
                    return List.of(new ReplyMessage(user.getTgId(), "Баллы установлены", null));
                }catch (NumberFormatException e) {
                    return List.of(new ReplyMessage(user.getTgId(), "Некорректный ввод. (Пример +5 или -3)", null));
                }
        }
        return List.of();
    }
}
