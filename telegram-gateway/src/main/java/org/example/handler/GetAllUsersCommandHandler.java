package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.TelegramUser;
import org.example.entity.dto.UserDto;
import org.example.entity.util.ReplyMessage;
import org.example.service.output.UserOutputService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllUsersCommandHandler implements CommandHandler{

    private final UserOutputService service;

    @Override
    public String getCommandName() {
        return "getAllUsers";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            return List.of(new ReplyMessage(user.getTgId(), "НЕТ ДОСТУПА.", null));
        }
        List<UserDto> allUsers = service.getAllUsers();
        Long countUsers = service.getCountUsers();
        StringBuilder sb = new StringBuilder();
        sb.append("Всего пользователей: ").append(countUsers).append("\n");
        for (UserDto dto : allUsers) {
            sb.append(dto.toString());
        }
        return List.of(new ReplyMessage(user.getTgId(), sb.toString(), null));
    }
}
