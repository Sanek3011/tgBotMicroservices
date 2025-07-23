package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.controller.TGBot;
import org.example.kafka.events.UserNotificationEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final TGBot bot;

    public void sendMessage(UserNotificationEvent event) {
        bot.sendMessageToUser(event.getTgId(), event.getMessage());
    }
}
