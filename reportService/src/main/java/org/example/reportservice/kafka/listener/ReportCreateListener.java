package org.example.reportservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.kafka.event.ReportCreateEvent;
import org.example.reportservice.service.ReportEventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportCreateListener {

    private final ReportEventService reportService;

    @KafkaListener(topics = "report.create",
            properties = {
                    "spring.json.value.default.type=org.example.reportservice.kafka.event.ReportCreateEvent",
            })
    public void listenNewReports(ReportCreateEvent event){
        reportService.processCreateEvent(event);
    }
}
