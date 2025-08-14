package org.example.util;

import lombok.experimental.UtilityClass;
import org.example.entity.dto.OrderDto;

@UtilityClass
public class OrderDtoFormatter {

    public static String format(OrderDto orderDto) {
        return String.format("id: %d Заказчик: %s, заказ: %s, дата заказа: %s", orderDto.getId(),
                orderDto.getName(), orderDto.getItemDesc(), orderDto.getDate());
    }
}
