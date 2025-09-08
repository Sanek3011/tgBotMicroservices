package org.example.service.output;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.dto.ItemDto;
import org.example.entity.dto.OrderDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
@Slf4j
@Service
public class OrdersOutputService {

    private final RestTemplate restTemplate;

    public OrdersOutputService(@Qualifier("orderService") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<OrderDto> getUncheckedOrders(Integer size, Integer page) {
        String url = UriComponentsBuilder.fromPath("/order")
                .queryParam("size", size)
                .queryParam("page", page)
                .toUriString();
        try {
            ResponseEntity<List<OrderDto>> exchange = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}

            );
            return exchange.getBody();
        }catch (HttpClientErrorException e) {
            log.error("Ошибка при получении списка заказов очков {} {}", size, page);
            return Collections.emptyList();
        }catch (RestClientException e) {
            log.error("Внутрення ошибка", e);
            return Collections.emptyList();
        }
    }
    public List<ItemDto> getAllItems() {
        ResponseEntity<List<ItemDto>> exchange = restTemplate.exchange(
                "/item",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }

        );
        return exchange.getBody();
    }
}
