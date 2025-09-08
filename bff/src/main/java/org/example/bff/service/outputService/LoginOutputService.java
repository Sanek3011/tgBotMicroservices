package org.example.bff.service.outputService;

import org.example.bff.security.JWTResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class LoginOutputService {

    private final WebClient client;

    public LoginOutputService(@Qualifier("userService") WebClient client) {
        this.client = client;
    }

    public Mono<JWTResponse> getJwtToken(String token) {
        return client.post()
                .uri("/login/jwt")
                .bodyValue(Map.of("token", token))
                .retrieve()
                .bodyToMono(JWTResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().is4xxClientError()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid"));
                    }
                    return Mono.empty();
                });
    }

    public Mono<JWTResponse> refreshToken(String token) {
        return client.post()
                .uri("/login/jwt/refresh")
                .bodyValue(Map.of("refreshToken", token))
                .retrieve()
                .bodyToMono(JWTResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().is4xxClientError()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is invalid"));
                    }
                    return Mono.empty();
                });
    }
}
