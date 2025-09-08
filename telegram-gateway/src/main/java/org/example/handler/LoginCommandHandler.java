package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.example.service.output.LoginOutputService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoginCommandHandler implements CommandHandler {

    private final LoginOutputService outputService;
    private final String frontUrl;

    public LoginCommandHandler(@Value("${services-web.bff-url}") String frontUrl, LoginOutputService outputService) {
        this.frontUrl = frontUrl;
        this.outputService = outputService;
    }

    @Override
    public String getCommandName() {
        return "login";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (user.getRole().equals(Role.GUEST)) {
            return List.of(new ReplyMessage(user.getTgId(), "Нет доступа", null));
        }
        String oneTimeLoginUrl = outputService.getOneTimeLoginUrl(user.getTgId());
        return List.of(new ReplyMessage(user.getTgId(), frontUrl+oneTimeLoginUrl, null));
    }
}
