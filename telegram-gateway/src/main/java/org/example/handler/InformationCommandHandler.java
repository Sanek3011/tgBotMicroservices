package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.TelegramUser;
import org.example.entity.dto.ItemDto;
import org.example.entity.util.ReplyMessage;
import org.example.service.KeyboardService;

import org.example.service.output.OrdersOutputService;
import org.example.service.output.UserOutputService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InformationCommandHandler implements CommandHandler {

    private final OrdersOutputService ordersOutputService;
    private final UserOutputService userOutputService;
    private final KeyboardService keyboardService;
    @Override
    public List<ReplyMessage> handle(String data, TelegramUser user) {

        ReplyMessage message;
        switch (data) {
            case "info":
                String info = getInfo(user);
                message = ReplyMessage.builder()
                        .text(info)
                        .chatId(user.getTgId())
                        .keyboard(null).build();
                return List.of(message);
            case "shop":
                List<ItemDto> allItems = ordersOutputService.getAllItems();
                if (allItems == null) {
                    return List.of(new ReplyMessage(user.getTgId(), "Товары не найдены", null));
                }
                keyboardService.getShopKeyboard(allItems);
                message = ReplyMessage.builder()
                        .text(getShopInfo(user, allItems))
                        .chatId(user.getTgId())
                        .keyboard(keyboardService.getShopKeyboard(allItems)).build();
                return List.of(message);
        }
        return null;
    }
    private String getInfo(TelegramUser user) {
        StringBuilder sb = new StringBuilder();
        sb.append("Добро пожаловать в БОТ JFMC05\nНикнейм в боте должен совпадать с игровым \nСписок доступных команд - /keyboard, выход из меню введите /quit \nПо поводу работы бота писать в discord sanek3011\n");
        if (user.getRole().equals(Role.GUEST)) {
            return sb.toString();
        }
        sb.append("--------------------------------\n");
        sb.append("Информация об аккаунте:\n");
        Integer score = userOutputService.getScoreFromUserService(user.getUserId().toString(), false).orElse(0);
        sb.append("Количество баллов: ").append(score + "\n");
        return sb.toString();
    }

    private String getShopInfo(TelegramUser user, List<ItemDto> allItems) {

        Integer score = userOutputService.getScoreFromUserService(user.getUserId().toString(), false).orElse(0);
        StringBuilder sb = new StringBuilder();
        sb.append("Ваши баллы:").append(score).append("\n");
        sb.append("--------------------------------\n");
        for (ItemDto item : allItems) {
            sb.append(item.toString());
        }
        return sb.toString();
    }

    @Override
    public String getCommandName() {
        return "info";
    }
}
