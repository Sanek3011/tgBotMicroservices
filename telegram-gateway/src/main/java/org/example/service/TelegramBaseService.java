package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.example.kafka.events.UserRegisteredEvent;
import org.example.kafka.events.UserRoleEvent;
import org.example.kafka.producer.RegisterUserProducer;
import org.example.repository.UserStateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBaseService {

    private final UserStateRepository userStateRepository;
    private final RegisterUserProducer registerUserProducer;

    public TelegramUser findByTgId(Long tgId) {
        return userStateRepository.findById(tgId).orElse(null);
    }

    @Transactional
    public void updateRole(Long tgId, String role) {
        TelegramUser user = userStateRepository.findById(tgId).orElseGet(() ->
                new TelegramUser(tgId, State.NO, Role.GUEST));
        user.setRole(Role.valueOf(role));
        userStateRepository.save(user);
    }


    @Transactional
    public void save(Long chatId) {
        if (!userStateRepository.existsById(chatId)) {
            TelegramUser user = new TelegramUser(chatId, State.NO, Role.GUEST);
            userStateRepository.save(user);
            registerUserProducer.sendRegistration(new UserRegisteredEvent(chatId));
        }
    }

    @Transactional
    public void saveAll(List<UserRoleEvent> users) {
        List<TelegramUser> list = users.stream()
                .map(u -> new TelegramUser(u.getTgId(), State.NO, Role.valueOf(u.getRole())))
                .toList();
        userStateRepository.saveAll(list);
    }
}
