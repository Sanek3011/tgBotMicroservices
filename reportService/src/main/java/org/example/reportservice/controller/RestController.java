package org.example.reportservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.reportservice.entity.dto.ReportActivityDto;
import org.example.reportservice.entity.dto.ReportAdminTgDto;
import org.example.reportservice.entity.dto.ReportTgDto;
import org.example.reportservice.service.ReportActivityService;
import org.example.reportservice.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RestController {

    private final ReportService reportService;
    private final ReportActivityService reportActivityService;

    @GetMapping("/reports/{userId}")
    public ResponseEntity<List<ReportTgDto>> getReportsByUserId(@PathVariable Long userId) {
        List<ReportTgDto> reports = reportService.getReportsByUserId(userId);
        if (reports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reports);

    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportAdminTgDto>> getAllReportsTgDto(@RequestParam(defaultValue = "5") Integer size,
                                                                     @RequestParam(defaultValue = "0") Integer page) {
        List<ReportAdminTgDto> allUserReports = reportService.getAllUserReports(size, page);
        if (allUserReports.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allUserReports);
    }
    @GetMapping("/reports/activity")
    public ResponseEntity<List<ReportActivityDto>> getReportActivity() {
        List<ReportActivityDto> allActivity = reportActivityService.findAllActivity();
        if (allActivity.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allActivity);
    }


}
