package org.example.reportservice.service;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.entity.ReportActivity;
import org.example.reportservice.entity.dto.ReportActivityDto;
import org.example.reportservice.entity.dto.ReportTgDto;
import org.example.reportservice.kafka.event.ReportActivityEvent;
import org.example.reportservice.repository.ReportActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportActivityService {

    private final ReportActivityRepository repository;

    public List<ReportActivityDto> findAllActivity() {
        List<ReportActivity> all = repository.findAll();
        return ReportActivityDto.toDtoList(all);
    }

    public ReportActivity findByType(String type) {
        if (!type.startsWith("REPORT_TYPE:")) {
            type = "REPORT_TYPE:"+type;
        }
        List<ReportActivity> byType = repository.findByType(type);
        if (byType.isEmpty()) {
            return null;
        }
        return byType.getFirst();
    }

    public void processReportActivity(ReportActivityEvent event) {
        switch (event.getEventType()) {
            case "update":
                updateReport(event);
                break;
            case "save":
                save(event);
                break;
            case "delete":
                delete(event);
                break;
        }
    }

    public void updateReport(ReportActivityEvent event) {
        ReportActivity reportActivity = repository.findById(event.getId()).orElseThrow();
        reportActivity.setBasePrice(event.getBasePrice());
        reportActivity.setCoefficient(event.getCoefficient());
        reportActivity.setDescription(event.getDescription());
        repository.save(reportActivity);
    }

    public void save(ReportActivityEvent event) {
        ReportActivity reportActivity = ReportActivity.builder()
                .basePrice(event.getBasePrice())
                .type(event.getType())
                .coefficient(event.getCoefficient())
                .description(event.getDescription())
                .build();
        repository.save(reportActivity);
    }

    public void delete(ReportActivityEvent event) {
        repository.deleteById(event.getId());
    }
}
