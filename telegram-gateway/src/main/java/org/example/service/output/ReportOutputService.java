package org.example.service.output;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.dto.ReportActivityDto;
import org.example.entity.dto.ReportAdminDto;
import org.example.entity.dto.ReportDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Slf4j
public class ReportOutputService {

    private final RestTemplate restTemplate;
    private final String reportApiUrl;
    private static final Integer SIZE = 5;

    public ReportOutputService(@Value("${services-rest.report-service-url}") String reportApiUrl, RestTemplate restTemplate) {
        this.reportApiUrl = reportApiUrl;
        this.restTemplate = restTemplate;
    }

    public List<ReportDto> getReportsByUserId(Long userId) {
        String url = reportApiUrl + "/reports/" + userId;
        ResponseEntity<List<ReportDto>> exchange = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ReportDto>>() {
        });
        if (exchange.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(204))) {
            return List.of();
        }
        return exchange.getBody();
    }

    public List<ReportActivityDto> getAllReportActivities() {
        String url = reportApiUrl+"/reports/activity";
        ResponseEntity<List<ReportActivityDto>> exchange = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ReportActivityDto>>() {
        });
        if (exchange.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(204))) {
            return List.of();
        }
        return exchange.getBody();
    }

    public List<ReportAdminDto> getAllReportsByPage(Integer page) {
        String url = UriComponentsBuilder.fromUriString(reportApiUrl)
                .path("/reports")
                .queryParam("page", page)
                .queryParam("size", SIZE)
                .toUriString();
        ResponseEntity<List<ReportAdminDto>> exchange = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        if (exchange.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(204))) {
            return List.of();
        }
        return exchange.getBody();
    }
}
