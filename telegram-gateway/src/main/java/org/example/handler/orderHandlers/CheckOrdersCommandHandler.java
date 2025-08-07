package org.example.handler.orderHandlers;

import lombok.RequiredArgsConstructor;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.example.entity.dto.ItemDto;
import org.example.entity.dto.OrderDto;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;
import org.example.service.KeyboardService;
import org.example.service.OutputService;
import org.example.service.TelegramBaseService;
import org.example.tempStorage.IntegerTempStorage;
import org.example.tempStorage.TempStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CheckOrdersCommandHandler implements CommandHandler {

    private final OutputService outputService;
    private final TempStorage<Integer> tempStorage;
    private final KeyboardService keyboardService;
    private final Integer SIZE = 1;
    private final TelegramBaseService baseService;

    @Override
    public String getCommandName() {
        return "checkOrder";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (!user.getState().equals(State.CHECK_ORDERS)) {
            baseService.updateUserState(user.getTgId(), State.CHECK_ORDERS);
        }

        if (update.equals("nextPage") || update.equals("previousPage")) {
            setPage(update, user.getTgId());
        }

        int page = tempStorage.get(user.getTgId()).orElse(0);
        List<OrderDto> uncheckedOrders = outputService.getUncheckedOrders(SIZE, page);
        setNamesInDtos(uncheckedOrders);
        return uncheckedOrders.stream()
                .map(u -> new ReplyMessage(user.getTgId(), u.toString(), keyboardService.getCheckOrdersButton(u.getId())))
                .toList();

    }

    private void setPage(String action, Long tgId) {
        Integer tmp = tempStorage.get(tgId).orElse(0);
        switch (action) {
            case "nextPage":
                tempStorage.put(tgId, ++tmp);
                break;
            case "previousPage":
                tempStorage.put(tgId, Math.max(--tmp, 0));
        }
    }

    private void setNamesInDtos(List<OrderDto> dtos) {
        Set<Long> userIds = dtos.stream()
                .map(OrderDto::getUserId)
                .collect(Collectors.toSet());
        Map<Long, String> nameMap = outputService.getNamesForOrdersById(userIds);
        dtos.forEach(o -> o.setName(nameMap.getOrDefault(o.getUserId(), "Unknown user")));
    }

}
