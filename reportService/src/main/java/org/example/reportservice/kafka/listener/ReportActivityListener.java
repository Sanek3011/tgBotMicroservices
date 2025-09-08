package org.example.reportservice.kafka.listener;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.kafka.event.ReportActivityEvent;
import org.example.reportservice.kafka.event.ReportCreateEvent;
import org.example.reportservice.service.ReportActivityService;
import org.example.reportservice.service.ReportEventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportActivityListener {

    private final ReportActivityService reportService;

    @KafkaListener(topics = "report.activity",
            properties = {
                    "spring.json.value.default.type=org.example.reportservice.kafka.event.ReportActivityEvent",
            })
    public void listenReportActivities(ReportActivityEvent event){
        if (event.getCoefficient() == null) {
            event.setCoefficient(false);
        }
        reportService.processReportActivity(event);
    }
}
