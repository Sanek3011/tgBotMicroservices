package org.example.userservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.events.UserRegisteredEvent;
import org.example.userservice.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationListener {

    private final UserService userService;

    @KafkaListener(topics = "user.registration",
            properties = {
                    "spring.json.value.default.type=org.example.userservice.kafka.events.UserRegisteredEvent",
            })
    public void listenUserRegistration(UserRegisteredEvent userRegisteredEvent) {
        System.out.println(">>>>>>>> ЛИСТЕНЕР");
        userService.saveUser(userRegisteredEvent);
    }
}
