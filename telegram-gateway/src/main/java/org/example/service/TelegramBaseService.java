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

import static org.example.entity.Role.ADMIN;

@Service
@RequiredArgsConstructor
public class TelegramBaseService {

    private final UserStateRepository userStateRepository;
    private final RegisterUserProducer registerUserProducer;

    public TelegramUser findByTgId(Long tgId) {
        TelegramUser user = userStateRepository.findByTgId(tgId).orElseGet(() ->
        {
            TelegramUser newUser = new TelegramUser();
            newUser.setTgId(tgId);
            newUser.setState(State.NO);
            return newUser;
        });
        return user;
    }

    public TelegramUser findById(Long id) {
        return userStateRepository.findByUserId(id);
    }

    @Transactional
    public void updateRole(UserRoleEvent event) {
        TelegramUser user = findByTgId(event.getTgId());
        user.setRole(Role.valueOf(event.getRole()));
        if (user.getUserId() == null) {
            user.setUserId(event.getId());
        }
        userStateRepository.save(user);
    }

    @Transactional
    public void updateUserState(Long tgId, State state) {
        userStateRepository.updateUserState(tgId, state);
    }


    @Transactional
    public void save(Long chatId) {
        if (!userStateRepository.existsById(chatId)) {
            TelegramUser user = new TelegramUser();
            user.setTgId(chatId);
            user.setState(State.NO);
            user.setRole(Role.GUEST);
            userStateRepository.save(user);
            registerUserProducer.sendRegistration(new UserRegisteredEvent(chatId));
        }
    }

    @Transactional
    public void saveAll(List<UserRoleEvent> users) {
        List<TelegramUser> list = users.stream()
                .map(u -> new TelegramUser(u.getTgId(), u.getId(), State.NO, Role.valueOf(u.getRole())))
                .toList();
        userStateRepository.saveAll(list);
    }

    public List<Long> findAllIdByRoles(List<String> roles) {
        List<Role> roleList = roles.stream()
                .map(Role::valueOf)
                .toList();
        return userStateRepository.findAllIdByRole(roleList);
    }
}
