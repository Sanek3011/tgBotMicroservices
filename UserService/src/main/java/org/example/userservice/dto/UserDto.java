package org.example.userservice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.userservice.entity.User;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserDto {

    Long id;
    String name;
    Long telegramId;
    String role;
    Integer score;


    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .telegramId(user.getTelegramId())
                .role(user.getRole().toString())
                .score(user.getScore())
                .build();
    }

    public static List<UserDto> toDtoList(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }


}
