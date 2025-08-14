package org.example.handler.orderHandlers;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.example.entity.dto.OrderDto;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;
import org.example.service.*;
import org.example.service.output.OrdersOutputService;
import org.example.service.output.UserOutputService;
import org.example.util.OrderDtoFormatter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CheckOrdersCommandHandler implements CommandHandler {

    private final OrdersOutputService ordersOutputService;
    private final UserOutputService userOutputService;
    private final PaginationService paginationService;
    private final KeyboardService keyboardService;
    private final Integer SIZE = 1;
    private final TelegramBaseService baseService;

    @Override
    public String getCommandName() {
        return "showOrders";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            return List.of(new ReplyMessage(user.getTgId(), "НЕТ ДОСТУПА.", null));
        }
        if (!user.getState().equals(State.CHECK_ORDERS)) {
            baseService.updateUserState(user.getTgId(), State.CHECK_ORDERS);
        }


        int page = paginationService.getCurrentPage(user.getTgId());
        List<OrderDto> uncheckedOrders = ordersOutputService.getUncheckedOrders(SIZE, page);
        setNamesInDtos(uncheckedOrders);
        return uncheckedOrders.stream()
                .map(u -> new ReplyMessage(user.getTgId(), OrderDtoFormatter.format(u), keyboardService.getCheckOrdersButton(u.getId())))
                .toList();

    }

    private void setNamesInDtos(List<OrderDto> dtos) {
        Set<Long> userIds = dtos.stream()
                .map(OrderDto::getUserId)
                .collect(Collectors.toSet());
        Map<Long, String> nameMap = userOutputService.getNamesById(userIds);
        dtos.forEach(o -> o.setName(nameMap.getOrDefault(o.getUserId(), "Unknown user")));
    }

}
