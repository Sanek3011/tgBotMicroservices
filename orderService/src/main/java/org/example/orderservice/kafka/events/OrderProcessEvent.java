package org.example.orderservice.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProcessEvent {

    private UUID id;
    private Long orderId;
    private String status;
    private Long initiatorId;

}
