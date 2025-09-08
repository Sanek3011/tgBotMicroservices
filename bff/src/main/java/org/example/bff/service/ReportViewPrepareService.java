package org.example.bff.service;

import lombok.RequiredArgsConstructor;
import org.example.bff.dto.ReportBigDto;
import org.example.bff.service.outputService.UserOutputService;
import org.example.bff.tempStorage.TempStorage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportViewPrepareService {


    private final UserNameFiller<ReportBigDto> userNameFiller;

    public Mono<ReportBigDto> prepareReport(ReportBigDto reportBigDto) {
        return userNameFiller.fillNames(reportBigDto);
    }

    public Flux<ReportBigDto> fillNames(Flux<ReportBigDto> dtos) {
        return userNameFiller.fillNames(dtos);
    }


}
