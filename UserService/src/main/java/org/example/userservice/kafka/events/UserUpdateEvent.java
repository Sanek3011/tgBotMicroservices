package org.example.userservice.kafka.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateEvent {
    Long tgId;
    String name;
    String role;
    Integer score;
}
