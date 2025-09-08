package org.example.bff.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserDto {

    Long id;
    String name;
    Long telegramId;
    String role;
    Integer score;

}
