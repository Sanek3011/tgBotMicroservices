package org.example.bff.controller;

import lombok.RequiredArgsConstructor;
import org.example.bff.dto.ReportBigDto;
import org.example.bff.kafka.events.reportEvents.ReportCreateEvent;
import org.example.bff.kafka.events.reportEvents.ReportProcessEvent;
import org.example.bff.kafka.producer.reportProducers.ReportCreateProducer;
import org.example.bff.kafka.producer.reportProducers.ReportProcessProducer;
import org.example.bff.security.AuthUser;
import org.example.bff.service.outputService.ReportOutputService;
import org.example.bff.service.ReportViewPrepareService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportCreateProducer producer;
    private final ReportOutputService reportOutputService;
    private final ReportViewPrepareService prepareService;
    private final ReportProcessProducer reportProcessProducer;

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'LEADER', 'USER')")
    @GetMapping("/create")
    public Mono<String> createReport(Model model){
        return reportOutputService.getActivities()
                .collectList()
                .map(list -> {
                    model.addAttribute("types", list);
                    return "createReport";
                });
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'LEADER', 'USER')")
    @PostMapping("/create")
    public Mono<String> createReportPost(@AuthenticationPrincipal AuthUser authUser,
                                   ReportCreateEvent event,
                                         Model model) {
        event.setUserId(Long.parseLong(authUser.getUserId()));
        if (event.getQuantity() == null) {
            event.setQuantity(1);
        }
        return producer.sendEvent(event)
                .then(Mono.fromRunnable(() -> model.addAttribute("message", "Успешно отправлено")))
                .thenReturn("createReport");

    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'LEADER', 'USER')")
    @GetMapping("/myReports")
    public Mono<String> myReports(@AuthenticationPrincipal AuthUser authUser,
                                  Model model) {
        String userId = authUser.getUserId();
        Flux<ReportBigDto> reports = reportOutputService.getReportsByUserId(Long.parseLong(userId))
                        .transform((Flux<ReportBigDto> flux) -> prepareService.fillNames(flux));
        model.addAttribute("reports", new ReactiveDataDriverContextVariable(reports, 1));
        return Mono.just("reports");
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'LEADER')")
    @GetMapping
    public Mono<String> allReports(Model model) {
        Flux<ReportBigDto> allReports = reportOutputService.getAllReports()
                        .transform((Flux<ReportBigDto> flux) -> prepareService.fillNames(flux));
        model.addAttribute("reports", new ReactiveDataDriverContextVariable(allReports, 10));
        return Mono.just("reports");
    }

    @GetMapping("/{id}")
    public Mono<String> reportById(@PathVariable Long id, Model model) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(ctx -> {
                    AuthUser principal = (AuthUser) ctx.getAuthentication().getPrincipal();
                    return reportOutputService.getReportById(id)
                            .filter(report -> principal.getRole().equals("ADMIN")
                            || principal.getRole().equals("LEADER")
                            || report.getUserId().toString().equals(principal.getUserId()))
                            .switchIfEmpty(Mono.error(new AccessDeniedException("Нет доступа")))
                            .flatMap(prepareService::prepareReport)
                            .doOnNext(report -> model.addAttribute("report", report))
                            .thenReturn("report");
                });
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'LEADER')")
    @PostMapping(value = "/{id}")
    public Mono<String> processReport(@PathVariable Long id,
                                      ServerWebExchange exchange) {
      return exchange.getFormData()
              .flatMap(f -> {
              String action = f.getFirst("action");
                  reportProcessProducer.sendEvent(new ReportProcessEvent(id, action));
                  return Mono.just("redirect:/reports/"+id);
              });
    }

}
