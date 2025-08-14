package org.example.reportservice.service;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.entity.ReportActivity;
import org.example.reportservice.entity.dto.ReportActivityDto;
import org.example.reportservice.entity.dto.ReportTgDto;
import org.example.reportservice.repository.ReportActivityRepository;
import org.springframework.stereotype.Service;

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
        List<ReportActivity> byType = repository.findByType(type);
        if (byType.isEmpty()) {
            return null;
        }
        return byType.getFirst();
    }
}
