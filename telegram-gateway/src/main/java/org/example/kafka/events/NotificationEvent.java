package org.example.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

    private UUID id;
    private Long userId;
    private Long tgId;
    private String message;
    private List<String> roles;
}
