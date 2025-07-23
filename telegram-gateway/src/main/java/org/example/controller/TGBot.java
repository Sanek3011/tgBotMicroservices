package org.example.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.kafka.events.UserRoleEvent;
import org.example.kafka.events.UserUpdateEvent;
import org.example.kafka.producer.UpdateUserProducer;
import org.example.service.KeyboardService;
import org.example.service.RoleSyncStartService;
import org.example.service.TelegramBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TGBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String BOT_NAME;
    @Value("${telegram.bot.token}")
    private String BOT_TOKEN;
    private final TelegramBaseService telegramBaseService;
    private final UpdateUserProducer producer;
    private final KeyboardService keyboardService;
    private final RoleSyncStartService roleSyncStartService;

    @PostConstruct
        public void init() {
        roleSyncStartService.syncRoles();
        registerCommands();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        String text = update.getMessage().getText();
        if ("/start".equals(text)) {
            telegramBaseService.save(chatId);
        }
        if (text.startsWith("/nickname")) {
            String[] mess = text.split(" ");
            UserUpdateEvent event = new UserUpdateEvent();
            event.setName(mess[1]);
            event.setTgId(chatId);
            producer.sendUpdate(event);
        }

        if (text.startsWith("/role")) {
            String[] mess = text.split(" ");
        }
        sendKeyboard(chatId, keyboardService.getKeyboardMarkup(chatId, false));
    }

    public Long getChatId(Update update) {
        if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getMessage().getChatId();
        }else{
            return update.getMessage().getChatId();
        }
    }

    public void sendMessageToUser(Long chatId, String text) {
        if (chatId != null) {
//            log.info("Отправка сообщения к {}", chatId);
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text);
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
//                log.warn("Отправка сообщения не удалась к {}", chatId);
            }
        }
    }
    public void sendKeyboard(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Нажмите на одну из кнопок");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
//            log.warn("Ошибка при отправке клавиатуры");
        }
    }

    private void registerCommands() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/keyboard", "Показать действия"));
        commands.add(new BotCommand("/nickname", "Установить имя"));
        commands.add(new BotCommand("/quit", "Выйти в меню(обнулить действие)"));


        try {
            this.execute(new SetMyCommands(commands, null, null));
        }catch (TelegramApiException e) {
            e.printStackTrace();
//            log.warn("Команды не зарегистрированы. registerCommands()");
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
