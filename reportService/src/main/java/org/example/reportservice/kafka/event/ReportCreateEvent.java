package org.example.reportservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportCreateEvent {

    private Long userId;
    private String desc;
    private String type;
    private Integer quantity;
    private String url;
}