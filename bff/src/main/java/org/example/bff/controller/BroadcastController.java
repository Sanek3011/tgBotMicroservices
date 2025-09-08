package org.example.bff.controller;

import lombok.RequiredArgsConstructor;
import org.example.bff.service.BroadcastService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/broadcast")
@PreAuthorize(value = "hasRole('ADMIN')")
public class BroadcastController {

    private final BroadcastService broadcastService;

    @GetMapping
    public Mono<String> broadcastGet(){
        return Mono.just("broadcast");
    }

    @PostMapping
    public Mono<String> sendAll(ServerWebExchange exchange){
        return exchange.getFormData()
                .flatMap(f -> {
                    List<String> role = f.get("roles");
                    String mess = f.getFirst("message");
                    String telegramId = f.getFirst("telegramId");
                    broadcastService.prepareBroadcastEvent(role, mess, telegramId);
                    return Mono.just("success");
                });
    }
}
