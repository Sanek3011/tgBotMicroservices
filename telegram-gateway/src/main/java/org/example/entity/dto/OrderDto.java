package org.example.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    Long id;
    Long userId;
    String name;
    String itemDesc;
    LocalDateTime date;

    @Override
    public String toString() {
        return String.format("id: %d Заказчик: %s, заказ: %s, дата заказа: %s", id, name, itemDesc, date);
    }
}
