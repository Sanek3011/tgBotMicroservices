package org.example.userservice.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleEvent {
    private UUID eventId;
    private Long id;
    private Long tgId;
    private String role;
}
