package org.example.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserRoleEvent {
    private UUID eventId;
    private Long id;
    private Long tgId;
    private String role;
}
