package org.example.bff.controller;

import lombok.RequiredArgsConstructor;
import org.example.bff.service.outputService.UserOutputService;
import org.example.bff.service.UserUpdateProcessService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserOutputService userOutputService;
    private final UserUpdateProcessService userUpdateProcessService;


    @GetMapping
    public Mono<String> getAllUsers(Model model) {
       return Mono.zip(userOutputService.getAllUsers().collectList(), userOutputService.getAllRoles())
               .map(tuple -> {
                   model.addAttribute("users", tuple.getT1());
                   model.addAttribute("roless", tuple.getT2());
                   return "users";
               });
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'LEADER')")
    @PostMapping("/update/{id}")
    public Mono<String> updateUser(@PathVariable Long id,
                             ServerWebExchange exchange) {
        return exchange.getFormData()
                        .flatMap(f -> {
                            userUpdateProcessService.prepareUserEvent(id,
                                    f.getFirst("name"),
                                    f.getFirst("telegramId"),
                                    f.getFirst("role"),
                                    f.getFirst("score"));
                            return Mono.just("redirect:/users");
                        });

    }
}
