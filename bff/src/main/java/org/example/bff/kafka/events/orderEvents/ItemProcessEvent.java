package org.example.bff.kafka.events.orderEvents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemProcessEvent {
    private Integer id;
    private String desc;
    private String type;
    private Integer price;
    private String eventType;
}
