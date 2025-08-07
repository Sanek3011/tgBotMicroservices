package org.example.handler.reportHandlers;

import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;

import java.util.List;

public class GetMyReportsCommandHandler implements CommandHandler {

    @Override
    public String getCommandName() {
        return "getMyReports";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        return List.of();
    }
}
