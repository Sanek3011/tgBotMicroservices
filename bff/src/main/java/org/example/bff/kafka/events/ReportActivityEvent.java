package org.example.bff.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportActivityEvent {
    Integer id;
    String type;
    String description;
    Integer basePrice;
    Boolean coefficient;
    String eventType;
}
