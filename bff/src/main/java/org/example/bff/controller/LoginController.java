package org.example.bff.controller;

import lombok.RequiredArgsConstructor;
import org.example.bff.service.outputService.LoginOutputService;
import org.example.bff.service.TokenCookieService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginOutputService outputService;
    private final TokenCookieService tokenCookieService;


    @GetMapping
    public Mono<String> login(@RequestParam String t, ServerWebExchange exchange) {
        return outputService.getJwtToken(t)
                .flatMap(jwt -> {
                    tokenCookieService.addAuthCookies(exchange, jwt);
                    return Mono.just("redirect:/");
                })
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
    }

}
