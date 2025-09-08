package org.example.reportservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.entity.dto.ReportActivityDto;
import org.example.reportservice.entity.dto.ReportAdminTgDto;
import org.example.reportservice.service.ReportActivityService;
import org.example.reportservice.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class RestControllerWeb {

    private final ReportService reportService;
    private final ReportActivityService reportActivityService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReportAdminTgDto>> getReportByUserId(@PathVariable Long userId) {
        List<ReportAdminTgDto> reportsByUserIdExtra = reportService.getReportsByUserIdExtra(userId);
        return ResponseEntity.ok(reportsByUserIdExtra);
    }

    @GetMapping
    public ResponseEntity<List<ReportAdminTgDto>> getAllReports() {
        List<ReportAdminTgDto> allReports = reportService.getAllReports();
        return ResponseEntity.ok(allReports);
    }

    @GetMapping("/report/{reportId}")
    public ResponseEntity<ReportAdminTgDto> getReportByReportId(@PathVariable Long reportId) {
        ReportAdminTgDto report = reportService.findByReportId(reportId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/activity")
    public ResponseEntity<List<ReportActivityDto>> getReportActivity() {
        List<ReportActivityDto> allActivity = reportActivityService.findAllActivity();
        if (allActivity.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allActivity);
    }
}
