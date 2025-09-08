package org.example.bff.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto implements HasUser{
    Long id;
    Long userId;
    String itemDesc;
    LocalDateTime date;
    String status;
    String userName;
}
