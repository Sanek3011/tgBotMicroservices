package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.TelegramUser;
import org.example.entity.dto.ItemDto;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final TelegramBaseService telegramBaseService;

//    private final ItemService itemService;
//    private final ReportActivityService reportActivityService;

    private final List<List<InlineKeyboardButton>> ADMIN_BUTTONS = List.of(
            List.of(createButton("Список пользователей", "getAllUsers"),
                    createButton("Все отчеты", "getAllReports")),
            List.of(createButton("Отчеты за прошлую неделю", "getReportsByUsernameAndDate"),
                    createButton("Удалить пользователя", "deleteUser")),
            List.of(createButton("Изменить роль пользователю", "changeRole"),
                    createButton("Масс.Рассылка", "sendAll")),
            List.of(createButton("Установить баллы", "modifyScore"),
                    createButton("Заказы за баллы", "checkOrder")),
            List.of(createButton("Назад", "backToMain")) // Кнопка для выхода из админ-меню
    );

    private final List<List<InlineKeyboardButton>> LEADER_BUTTONS = List.of(
            List.of(createButton("Все отчеты", "getAllReports"),
                    createButton("Отчеты за прошлую неделю", "getReportsByUsernameAndDate")),
            List.of(createButton("Изменить роль пользователю", "changeRole"),
                    createButton("Назад", "backToMain"))
    );

    private final List<List<InlineKeyboardButton>> USER_BUTTON = List.of(
            List.of(createButton("Магазин", "shop"),
                    createButton("Логин", "login")),
            List.of(createButton("Отправить отчет", "createReport"),
                    createButton("Список моих отчетов", "getMyReports")),
            List.of(createButton("Обновить свободные вакансии", "updateRanks"))
    );

    private final List<List<InlineKeyboardButton>> GUEST_BUTTON = List.of(
            List.of(createButton("Информация", "info"),
                    createButton("Вакансии и з/п", "salaries"))
    );

//    public KeyboardService(ItemService itemService, ReportActivityService reportActivityService) {
//        this.itemService = itemService;
//        this.reportActivityService = reportActivityService;
//    }

    public InlineKeyboardButton createButton(String text, String callback) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callback);
        return button;

    }

    public InlineKeyboardMarkup getCheckOrdersButton(Long id) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(List.of(createButton("<<", "previousPage"),
                createButton("EXIT", "backToMain"),
                createButton(">>", "nextPage")));
        rowsInLine.add(List.of(createButton("Одобрить", "checkOrder_ACCEPTED_"+id),
                createButton("Выполнено", "checkOrder_DONE_"+id),
                createButton("Отказать", "checkOrder_REJECTED_"+id)));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rowsInLine);
        return markup;
    }

    public InlineKeyboardMarkup getPaginationKeyboard() {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(List.of(createButton("<<", "previousPage"),
                createButton("EXIT", "backToMain"),
                createButton(">>", "nextPage")));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rowsInLine);
        return markup;
    }

    public InlineKeyboardMarkup getShopKeyboard(List<ItemDto> allItems) {

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        for (int i = 0; i < allItems.size(); i++) {
            InlineKeyboardButton button = createButton(allItems.get(i).getDesc(), "ORDER_"+allItems.get(i).getType());
            buttons.add(button);
            if (i % 2 == 0 || i == allItems.size() - 1) {
                rowsInLine.add(buttons);
                buttons = new ArrayList<>();
            }
        }
        return new InlineKeyboardMarkup(rowsInLine);

    }
//    public InlineKeyboardMarkup getReportTypeButtons() {
//        List<ReportActivityDto> reportActivityDtoList = reportActivityService.findAllDtos();
//
//        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
//        List<InlineKeyboardButton> buttons = new ArrayList<>();
//
//        for (int i = 0; i < reportActivityDtoList.size(); i++) {
//            InlineKeyboardButton button = createButton(reportActivityDtoList.get(i).getDescription(), reportActivityDtoList.get(i).getType());
//            buttons.add(button);
//            if (i % 2 == 0 || i == reportActivityDtoList.size() - 1) {
//                rowsInLine.add(buttons);
//                buttons = new ArrayList<>();
//            }
//        }
//        return new InlineKeyboardMarkup(rowsInLine);
//
//    }



    public InlineKeyboardMarkup getKeyboardMarkup(Long tgId, boolean isSpecialMenu) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        TelegramUser user = telegramBaseService.findByTgId(tgId);

        switch (user.getRole()) {
            case ADMIN:
                if (isSpecialMenu) {
                    rowsInline.addAll(ADMIN_BUTTONS);
                }else{
                    rowsInline.add(List.of(createButton("Админ панель", "adminPanel")));
                    rowsInline.addAll(USER_BUTTON);
                    rowsInline.addAll(GUEST_BUTTON);
                }
                break;
            case LEADER:
                if (isSpecialMenu) {
                    rowsInline.addAll(LEADER_BUTTONS);
                    break;
                }else {
                    rowsInline.add(List.of(createButton("Панель лидера", "leaderPanel")));

                }
            case USER:
                rowsInline.addAll(USER_BUTTON);
            case GUEST:
                rowsInline.addAll(GUEST_BUTTON);
        }
        Collections.reverse(rowsInline);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rowsInline);
        return keyboard;

    }

}
