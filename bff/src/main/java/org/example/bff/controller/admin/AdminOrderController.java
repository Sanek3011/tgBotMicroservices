package org.example.bff.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.bff.dto.OrderDto;
import org.example.bff.kafka.events.orderEvents.OrderProcessEvent;
import org.example.bff.kafka.producer.orderProducers.OrderProcessProducer;
import org.example.bff.service.OrderPrepareService;
import org.example.bff.service.outputService.OrderOutputService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/admin/orders")
@PreAuthorize(value = "hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderOutputService orderService;
    private final OrderPrepareService prepareService;
    private final OrderProcessProducer producer;


    @GetMapping
    public Mono<String> orders(Model model) {
        Flux<OrderDto> orders = orderService.getAllOrders()
                        .transform((Flux<OrderDto> flux) -> prepareService.fillNames(flux));
        model.addAttribute("orders", new ReactiveDataDriverContextVariable(orders, 10));
        return Mono.just("orders");
    }


    @GetMapping("/{id}")
    public Mono<String> getOrder(@PathVariable Long id, Model model) {
        Mono<OrderDto> orderMono = orderService.getOrderById(id)
                .flatMap(prepareService::prepareOrder);
        model.addAttribute("order", orderMono);
        return Mono.just("order");
    }

    @PostMapping("{id}")
    public Mono<String> processOrder(@PathVariable Long id,
                               ServerWebExchange exchange) {
        return exchange.getFormData()
                .flatMap(f -> {
                    String status = f.getFirst("status");
                    producer.sendEvent(new OrderProcessEvent(id, status, null));
                    return Mono.just("success");
                });
    }
}
