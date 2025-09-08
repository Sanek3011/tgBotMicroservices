package org.example.bff.service.outputService;

import org.example.bff.dto.ItemDto;
import org.example.bff.dto.OrderDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrderOutputService {

    private final WebClient client;

    public OrderOutputService(@Qualifier("orderService") WebClient client) {
        this.client = client;
    }

    public Flux<ItemDto> getAllItems() {
        return client.get()
                .uri("/item")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<>() {
                });
    }

    public Flux<OrderDto> getAllOrders() {
        return client.get()
                .uri("/order")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<OrderDto>() {});
    }

    public Mono<OrderDto> getOrderById(Long id) {
        return client.get()
                .uri("/order/" + id)
                .retrieve()
                .bodyToMono(OrderDto.class);
    }
}
