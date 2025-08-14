package org.example.reportservice.service;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.entity.Report;
import org.example.reportservice.entity.ReportActivity;
import org.example.reportservice.entity.Status;
import org.example.reportservice.kafka.event.ReportCreateEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportEventService {

    private final ReportService reportService;
    private final ReportActivityService reportActivityService;

    public void processEvent(ReportCreateEvent event) {
        ReportActivity activity = reportActivityService.findByType(event.getType());
        String desc = event.getDesc();
        Report build = Report.builder()
                .dateOfCreation(LocalDate.now())
                .userId(event.getUserId())
                .status(Status.NEW)
                .reportActivity(activity)
                .description(desc)
                .cost(event.getQuantity()*activity.getBasePrice())
                .url(event.getUrl())
                .build();
        reportService.save(build);



    }



}
