package org.example.kafka.listeners;

import lombok.RequiredArgsConstructor;
import org.example.kafka.events.UserRoleEvent;
import org.example.service.TelegramBaseService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateRoleListener {

    private final TelegramBaseService telegramBaseService;

    @KafkaListener(topics = "user.update.role",
            properties = {
                    "spring.json.value.default.type=org.example.kafka.events.UserRoleEvent",
            })
    public void changeRoleListen(UserRoleEvent event) {
        telegramBaseService.updateRole(event.getTgId(), event.getRole());
    }
}
