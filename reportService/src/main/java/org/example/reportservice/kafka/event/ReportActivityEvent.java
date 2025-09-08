package org.example.reportservice.kafka.event;

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
