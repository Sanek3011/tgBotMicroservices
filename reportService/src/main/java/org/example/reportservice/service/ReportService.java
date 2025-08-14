package org.example.reportservice.service;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.entity.Report;
import org.example.reportservice.entity.dto.ReportAdminTgDto;
import org.example.reportservice.entity.dto.ReportTgDto;
import org.example.reportservice.kafka.event.NotificationEvent;
import org.example.reportservice.kafka.event.ReportCreateEvent;
import org.example.reportservice.kafka.producer.NotificationProducer;
import org.example.reportservice.repository.ReportRepository;
import org.example.reportservice.service.notification.notification.NotificationRequest;
import org.example.reportservice.service.notification.notification.NotificationSender;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository repository;
    private final NotificationSender notificationSender;

    public List<ReportTgDto> getReportsByUserId(Long userId) {
        List<Report> reportList = repository.findByUserIdOrderByDateOfCreationDesc(userId);
        return ReportTgDto.toDtoList(reportList);
    }


    public List<ReportAdminTgDto> getAllUserReports(Integer size, Integer page) {
        List<Report> reportList = repository.findAll(PageRequest.of(page, size).withSort(Sort.by("dateOfCreation").descending())).getContent();
        return ReportAdminTgDto.toDtoList(reportList);
    }

    public void save(Report report) {
        repository.save(report);
        notificationSender.sendNotification(new NotificationRequest(report.getUserId(), "Ваш отчет успешно сохранен."));
    }

}
