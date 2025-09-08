package org.example.bff.service.outputService;

import org.example.bff.dto.UserDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserOutputService {

    private final WebClient client;

    public UserOutputService(@Qualifier("userService") WebClient client) {
        this.client = client;
    }

    public Mono<Map<Long, String>> getUsernames(Set<Long> ids) {
        return client.post()
                .uri("/users/names")
                .bodyValue(ids)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<Long, String>>() {});
    }

    public Flux<UserDto> getAllUsers() {
        return client.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(UserDto.class);
    }

    public Mono<List<String>> getAllRoles() {
        return client.get()
                .uri("/roles")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                });
    }

    public Mono<Integer> getScoreById(String id) {
        return client.get()
                .uri("/balance/"+id)
                .retrieve()
                .bodyToMono(Integer.class);
    }
}
