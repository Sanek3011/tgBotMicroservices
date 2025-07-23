package org.example.userservice.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceNotificationEvent {

    private Long tgId;
    private String message;
}
