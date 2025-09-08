package org.example.bff.service;

import lombok.RequiredArgsConstructor;
import org.example.bff.dto.OrderDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderPrepareService {

    private final UserNameFiller<OrderDto> userNameFiller;

    public Flux<OrderDto> fillNames(Flux<OrderDto> dtoFlux) {
        return userNameFiller.fillNames(dtoFlux);
    }
    public Mono<OrderDto> prepareOrder(OrderDto orderDto) {
        return userNameFiller.fillNames(orderDto);
    }
}
