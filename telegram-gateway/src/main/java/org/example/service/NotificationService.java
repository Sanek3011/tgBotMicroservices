package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.controller.TGBot;
import org.example.kafka.events.NotificationEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final TGBot bot;
    private final TelegramBaseService baseService;

    public void sendMessage(NotificationEvent event) {
        if (event.getRoles() != null && !event.getRoles().isEmpty()) {
            List<Long> allIdByRoles = baseService.findAllIdByRoles(event.getRoles());
            sendMessage(allIdByRoles, event.getMessage());
            return;
        }
        if (event.getTgId() != null) {
            sendMessage(List.of(event.getTgId()), event.getMessage());
        }else{
            Long tgId = baseService.findById(event.getUserId()).getTgId();
            sendMessage(List.of(tgId), event.getMessage());
        }
    }

    public void sendMessage(List<Long> tgId, String message) {
        for (Long tgIds : tgId) {
            bot.sendMessageToUser(tgIds, message);
        }
    }
}
