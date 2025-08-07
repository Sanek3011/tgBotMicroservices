package org.example.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Integer id;
    String desc;
    String type;
    Integer price;


    @Override
    public String toString() {
        return String.format("%s: стоимость %d балл(а)\n", desc, price);
    }
}
