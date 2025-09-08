package org.example.bff.service.outputService;

import org.example.bff.dto.ReportActivityDto;
import org.example.bff.dto.ReportBigDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReportOutputService {

    private final WebClient client;

    public ReportOutputService(@Qualifier("reportService") WebClient client) {
        this.client = client;
    }

    public Flux<ReportBigDto> getReportsByUserId(Long userId) {
        return client.get()
                .uri("/reports/"+userId)
                .retrieve()
                .bodyToFlux(ReportBigDto.class);
    }

    public Flux<ReportBigDto> getAllReports() {
        return client.get()
                .uri("/reports")
                .retrieve()
                .bodyToFlux(ReportBigDto.class);
    }

    public Mono<ReportBigDto> getReportById(Long reportId) {
        return client.get()
                .uri("/reports/report/" + reportId)
                .retrieve()
                .bodyToMono(ReportBigDto.class);
    }

    public Flux<ReportActivityDto> getActivities() {
        return client.get()
                .uri("/reports/activity")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ReportActivityDto>() {});
    }
}
