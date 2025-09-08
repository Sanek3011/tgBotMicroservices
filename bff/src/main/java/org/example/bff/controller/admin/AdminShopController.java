package org.example.bff.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.bff.dto.ItemDto;
import org.example.bff.kafka.events.orderEvents.ItemProcessEvent;
import org.example.bff.kafka.producer.ItemProcessProducer;
import org.example.bff.service.outputService.OrderOutputService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor

@Controller
@RequestMapping("/admin/shop")
@PreAuthorize(value = "hasRole('ADMIN')")
public class AdminShopController {

    private final OrderOutputService outputService;
    private final ItemProcessProducer producer;

    @GetMapping
    public Mono<String> shop(Model model) {
        Flux<ItemDto> allItems = outputService.getAllItems();
        model.addAttribute("items", new ReactiveDataDriverContextVariable(allItems));
        return Mono.just("shop");
    }

    @PostMapping("/edit/{id}")
    public Mono<String> updateShop(@PathVariable Integer id,
                                   @ModelAttribute ItemProcessEvent event) {
        event.setId(id);
        event.setEventType("update");
        producer.sendEvent(event);
        return Mono.just("redirect:/admin/shop");
    }

    @PostMapping("/create")
    public Mono<String> saveShop(@ModelAttribute ItemProcessEvent event){
        event.setEventType("save");
        producer.sendEvent(event);
        return Mono.just("redirect:/admin/shop");
    }

    @PostMapping("/delete/{id}")
    public Mono<String> deleteShop(@PathVariable Integer id) {
        ItemProcessEvent processEvent = new ItemProcessEvent();
        processEvent.setId(id);
        processEvent.setEventType("delete");
        producer.sendEvent(processEvent);
        return Mono.just("redirect:/admin/shop");

    }

}