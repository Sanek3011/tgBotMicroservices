package org.example.userservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.userservice.entity.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserTgVisualDto {

    Long id;
    String name;
    String role;
    Integer score;

    public static UserTgVisualDto toDto(User user) {
        return UserTgVisualDto.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole().toString())
                .score(user.getScore())
                .build();
    }

    public static List<UserTgVisualDto> toDtoList(List<User> users) {
        List<UserTgVisualDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }
}
