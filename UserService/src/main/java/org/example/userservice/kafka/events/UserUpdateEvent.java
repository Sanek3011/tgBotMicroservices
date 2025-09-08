package org.example.userservice.kafka.events;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateEvent {
    private UUID eventId;
    private Long id;
    private Long tgId;
    private String name;
    private String role;
    private Integer score;
    private Long initiatorTgId;
    private Boolean isWeb;
}
