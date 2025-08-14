package org.example.handler.reportHandlers;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;
import org.example.kafka.events.ReportCreateEvent;
import org.example.kafka.producer.CreateReportProducer;
import org.example.service.KeyboardService;
import org.example.service.TelegramBaseService;
import org.example.service.output.ReportOutputService;
import org.example.tempStorage.TempStorage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateReportCommandHandler implements CommandHandler {

    private final TempStorage<ReportCreateEvent> tempStorage;
    private final ReportOutputService reportOutputService;
    private final KeyboardService keyboardService;
    private final TelegramBaseService telegramBaseService;
    private final CreateReportProducer createReportProducer;

    @Override
    public String getCommandName() {
        return "createReport";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (user.getRole().equals(Role.GUEST)) {
            return List.of(new ReplyMessage(user.getTgId(), "НЕТ ДОСТУПА.", null));
        }
        switch (user.getState()) {
            case NO:
                telegramBaseService.updateUserState(user.getTgId(), State.WAITING_TYPE);
                return List.of(new ReplyMessage(user.getTgId(), "Выберите тип отчета", keyboardService.getReportTypeButtons(reportOutputService.getAllReportActivities())));
            case WAITING_TYPE:
                return List.of(getTypeOfReport(update, user));
            case WAITING_QUANTITY:
                return List.of(setQuantity(update, user));
            case WAITING_DESCRIPTION:
                return List.of(setDescription(update, user));
            case WAITING_IMG:
                return List.of(setImgUrl(update, user));

        }
        return List.of(new ReplyMessage(user.getTgId(), "непредвиденная ошибка. введите /quit и начните заново", null));
    }

    private ReplyMessage getTypeOfReport(String update, TelegramUser user) {
        String[] type = update.split("-"); // REPORT_TYPE:INVITES-
        ReportCreateEvent reportEvent = ReportCreateEvent.builder()
                .type(type[0].toUpperCase())
                .userId(user.getUserId())
                .build();
        if (type.length == 2) {
            telegramBaseService.updateUserState(user.getTgId(), State.WAITING_QUANTITY);
            tempStorage.put(user.getTgId(), reportEvent);
            return new ReplyMessage(user.getTgId(), "Введите кол-во инвайтов/продаж (должно совпадать со скринами)", null);
        }
        reportEvent.setQuantity(1);
        tempStorage.put(user.getTgId(), reportEvent);
        telegramBaseService.updateUserState(user.getTgId(), State.WAITING_DESCRIPTION);
        return new ReplyMessage(user.getTgId(), "Введите описание отчета", null);
    }

    private ReplyMessage setQuantity(String update, TelegramUser user) {
        try {
            ReportCreateEvent reportDto = tempStorage.get(user.getTgId()).orElseThrow();
            int i = Integer.parseInt(update);
            reportDto.setDesc("Количество:"+i);
            reportDto.setQuantity(i);
            tempStorage.put(user.getTgId(), reportDto);
            telegramBaseService.updateUserState(user.getTgId(), State.WAITING_IMG);
            return new ReplyMessage(user.getTgId(), "Прикрепите ссылку", null);
        } catch (NumberFormatException e) {
            return new ReplyMessage(user.getTgId(), "Некорректный ввод. Ожидается число", null);
        }
    }

    private ReplyMessage setDescription(String update, TelegramUser user) {
        ReportCreateEvent dto = tempStorage.get(user.getTgId()).orElseThrow();
        dto.setDesc(update);
        tempStorage.put(user.getTgId(), dto);
        telegramBaseService.updateUserState(user.getTgId(), State.WAITING_IMG);
        return new ReplyMessage(user.getTgId(), "Прикрепите ссылку", null);
    }

    private ReplyMessage setImgUrl(String update, TelegramUser user) {
        ReportCreateEvent repDto = tempStorage.get(user.getTgId()).orElseThrow();
        repDto.setUrl(update);
        createReportProducer.sendRequestOrder(repDto);
        tempStorage.remove(user.getTgId());
        telegramBaseService.updateUserState(user.getTgId(), State.NO);
        return new ReplyMessage(user.getTgId(), "Отчет в процессе отправки...", null);
    }
}
