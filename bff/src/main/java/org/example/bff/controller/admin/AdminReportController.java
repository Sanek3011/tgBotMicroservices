package org.example.bff.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.bff.kafka.events.ReportActivityEvent;
import org.example.bff.kafka.producer.reportProducers.ReportActivityProducer;
import org.example.bff.service.outputService.ReportOutputService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/report")
@PreAuthorize(value = "hasRole('ADMIN')")
public class AdminReportController {


    private final ReportOutputService reportOutputService;
    private final ReportActivityProducer producer;


    @GetMapping
    public Mono<String> getAdminReportMenu(Model model) {
        return reportOutputService.getActivities()
                .collectList()
                .map(list -> {
                    model.addAttribute("reportActivities", list);
                    return "reportManager";
                });
    }

    @PostMapping("/edit/{id}")
    public Mono<String> editReportActivity(@PathVariable Integer id,
                                     @ModelAttribute ReportActivityEvent event){
        event.setId(id);
        event.setEventType("update");
        if (event.getCoefficient() == null) {
            event.setCoefficient(false);
        }
        producer.sendEvent(event);
        return Mono.just("redirect:/admin/report");
    }


    @PostMapping("/delete/{id}")
    public Mono<String> deleteReportActivity(@PathVariable Integer id){
        producer.sendEvent(ReportActivityEvent.builder().id(id).eventType("delete").build());
        return Mono.just("success");
    }
    @PostMapping("/create")
    public Mono<String> createReportActivity(@ModelAttribute ReportActivityEvent dto) {
        dto.setType("REPORT_TYPE:"+dto.getType());
        dto.setEventType("save");
        producer.sendEvent(dto);
        return Mono.just("success");

    }
}
