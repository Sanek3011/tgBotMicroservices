package org.example.orderservice.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestedEvent {

    private UUID id;
    private String item;
    private Long tgId;
    private Integer price;
    private Long orderId;
}