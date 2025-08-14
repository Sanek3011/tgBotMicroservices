package org.example.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;
import org.example.kafka.events.UserRegisteredEvent;
import org.example.kafka.events.UserRoleEvent;
import org.example.kafka.events.UserUpdateEvent;
import org.example.kafka.producer.UpdateUserProducer;
import org.example.service.KeyboardService;
import org.example.service.PaginationService;
import org.example.service.RoleSyncStartService;
import org.example.service.TelegramBaseService;
import org.example.tempStorage.TempStorageManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TGBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String BOT_NAME;
    @Value("${telegram.bot.token}")
    private String BOT_TOKEN;
    private final TelegramBaseService telegramBaseService;
    private final UpdateUserProducer producer;
    private final KeyboardService keyboardService;
    private final RoleSyncStartService roleSyncStartService;
    private final Map<String, CommandHandler> commandHandlers;
    private final TempStorageManager tempStorageManager;
    private final PaginationService paginationService;


    @PostConstruct
        public void init() {
        roleSyncStartService.syncRoles();
        registerCommands();
    }

    public TGBot(List<CommandHandler> handlers,
                 KeyboardService keyboardService,
                 UpdateUserProducer producer,
                 RoleSyncStartService roleSyncStartService,
                 TelegramBaseService telegramBaseService,
                 TempStorageManager tempStorageManager, PaginationService paginationService) {
        this.commandHandlers = handlers.stream()
                .collect(Collectors.toMap(
                        CommandHandler::getCommandName,
                        Function.identity()
                ));
        this.keyboardService = keyboardService;
        this.producer = producer;
        this.roleSyncStartService = roleSyncStartService;
        this.telegramBaseService = telegramBaseService;
        this.tempStorageManager = tempStorageManager;
        this.paginationService = paginationService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        TelegramUser user = telegramBaseService.findByTgId(chatId);
        if (update.hasMessage() && update.getMessage().hasText()) {
            textHandler(update.getMessage().getText(),user);
        }else if (update.hasCallbackQuery()) {
            handleQuery(update.getCallbackQuery().getData(), user);
        }
    }

    private void textHandler(String text, TelegramUser user) {
        log.info("Получено сообщение от пользователя {}: {}", user.getTgId(), text);


        if ("/role".equals(text)) {
            String[] split = text.split(" ");
            UserUpdateEvent build = UserUpdateEvent.builder()
                    .name(split[1])
                    .role(split[2])
                    .eventId(UUID.randomUUID()).build();
            producer.sendUpdate(build); //// TODO: Убрать в последствии
        }

        if ("/quit".equals(text) || "quit".equals(text)) {
            telegramBaseService.updateUserState(user.getTgId(), State.NO);
            sendKeyboard(user.getTgId(), keyboardService.getKeyboardMarkup(user.getTgId(), false));
            tempStorageManager.clearForUser(user.getTgId());
            return;
        }


        if ("/keyboard".equals(text)) {
            sendKeyboard(user.getTgId(), keyboardService.getKeyboardMarkup(user.getTgId(), false));
            telegramBaseService.updateUserState(user.getTgId(), State.NO);
            log.info("Клавиатура НЕ админ отправлена к {}", user.getTgId());
            return;

        }

        if (text.startsWith("/nickname")) {
            String[] mass = text.split(" ");
            try {
                UserUpdateEvent event = new UserUpdateEvent();
                event.setEventId(UUID.randomUUID());
                event.setTgId(user.getTgId());
                event.setName(mass[1]);
                producer.sendUpdate(event);
                return;
            }catch (Exception e){
                sendMessageToUser(user.getTgId(), "Никнейм уже зарегистрирован");
            }
        }
        handleState(text, user);
    }

    private void handleState(String update, TelegramUser user) {

        switch (user.getState()) {
            case WAITING_NICKSCORE:
            case WAITING_SCOREKEYBOARD:
                executeMessageFromHandlers(commandHandlers.get("modifyScore").handle(update, user));
                break;
            case WAITING_CHANGEROLE:
                executeMessageFromHandlers(commandHandlers.get("changeRole").handle(update, user));
                break;
            case CHECK_ORDERS:
                executeMessageFromHandlers(commandHandlers.get("showOrders").handle(update, user));
                break;
            case CHECK_REPORTS:
                executeMessageFromHandlers(commandHandlers.get("getAllReports").handle(update, user));
                break;
            case WAITING_IMG:
            case WAITING_DESCRIPTION:
            case WAITING_QUANTITY:
                executeMessageFromHandlers(commandHandlers.get("createReport").handle(update, user));
        }
    }

    private void handleQuery(String data, TelegramUser user) {
        System.out.println(data);
        switch (data) {
            case String s when s.startsWith("REPORT_TYPE"):
                executeMessageFromHandlers(commandHandlers.get("createReport").handle(data ,user));
                break;
            case "leaderPanel":
            case "adminPanel":
                sendKeyboard(user.getTgId(), keyboardService.getKeyboardMarkup(user.getTgId(), true));
                break;
            case "shop":
                executeMessageFromHandlers(commandHandlers.get("info").handle(data, user));
                break;
            case String order when order.startsWith("ORDER_"):
                executeMessageFromHandlers(commandHandlers.get("createOrder").handle(data, user));
                break;
            case "backToMain":
                telegramBaseService.updateUserState(user.getTgId(), State.NO);
                tempStorageManager.clearForUser(user.getTgId());
                sendKeyboard(user.getTgId(), keyboardService.getKeyboardMarkup(user.getTgId(), false));
                break;
            case "previousPage":
                paginationService.previousPage(user.getTgId());
                handleState(data, user);
                break;
            case "nextPage":
                paginationService.nextPage(user.getTgId());
                handleState(data, user);
                break;
            case String checkOrder when checkOrder.startsWith("checkOrder"):
                executeMessageFromHandlers(commandHandlers.get("processOrder").handle(data, user));
                break;
            default:
            executeMessageFromHandlers(commandHandlers.get(data).handle(data, user));
        }
    }

    public void executeMessageFromHandlers(List<ReplyMessage> messages) {
        if (messages.isEmpty()) {
            return;
        }
        for (ReplyMessage mpl : messages) {
            String text = mpl.getText();
            Long chatId = mpl.getChatId();
            sendMessageToUser(chatId, text);
            if (mpl.getKeyboard() != null) {
                sendKeyboard(chatId, mpl.getKeyboard());
            }
        }
    }

    public static Long getChatId(Update update) {
        if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getMessage().getChatId();
        }else{
            return update.getMessage().getChatId();
        }
    }

    public void sendMessageToUser(Long chatId, String text) {
        if (chatId != null) {
            log.info("Отправка сообщения к {}", chatId);
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text);
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("Отправка сообщения не удалась к {}", chatId);
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
            log.warn("Ошибка при отправке клавиатуры");
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
            log.warn("Команды не зарегистрированы. registerCommands()");
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
