package org.example.bff.controller;

import lombok.RequiredArgsConstructor;
import org.example.bff.dto.ItemDto;
import org.example.bff.kafka.events.orderEvents.OrderRequestEvent;
import org.example.bff.kafka.producer.orderProducers.OrderRequestProducer;
import org.example.bff.security.AuthUser;
import org.example.bff.service.outputService.OrderOutputService;
import org.example.bff.service.outputService.UserOutputService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderOutputService outputService;
    private final UserOutputService userOutputService;
    private final OrderRequestProducer orderRequestProducer;


    @PreAuthorize(value = "hasAnyRole('ADMIN', 'LEADER', 'USER')")
    @GetMapping("/create")
    public Mono<String> makeOrder(@AuthenticationPrincipal AuthUser authUser, Model model) {
        Flux<ItemDto> allItems = outputService.getAllItems();
        Mono<Integer> score = userOutputService.getScoreById(authUser.getUserId());
        model.addAttribute("items", new ReactiveDataDriverContextVariable(allItems, 1));
        model.addAttribute("score", score);
        return Mono.just("createOrder");
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'LEADER', 'USER')")
    @PostMapping("/create")
    public Mono<String> saveOrder(@AuthenticationPrincipal AuthUser authUser, ServerWebExchange exchange) {
        return exchange.getFormData()
                .flatMap(f -> {
                    String type = f.getFirst("type");
                    orderRequestProducer.sendEvent(new OrderRequestEvent(type, Long.parseLong(authUser.getUserId())));
                    return Mono.just("process");
                });
    }
}
