package org.example.bff.kafka.events.reportEvents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportProcessEvent {

    private Long reportId;
    private String status;
}
