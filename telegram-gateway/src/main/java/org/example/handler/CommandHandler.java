package org.example.handler;

import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface CommandHandler {

    List<ReplyMessage> handle(String update, TelegramUser user);
    String getCommandName();
}
