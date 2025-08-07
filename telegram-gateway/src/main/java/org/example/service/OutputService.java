package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.config.ServicesRestSourceConfig;
import org.example.entity.dto.ItemDto;
import org.example.entity.dto.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@Slf4j
public class OutputService {

    private final RestTemplate restTemplate;
    private final ServicesRestSourceConfig properties;

    public OutputService(RestTemplate restTemplate, ServicesRestSourceConfig properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public Map<Long, String> getNamesForOrdersById(Set<Long> ids) {
        String url = UriComponentsBuilder.fromUriString(properties.getUserServiceUrl())
                .path("/users/names")
                .toUriString();
        try {
            HttpEntity<Set<Long>> setHttpEntity = new HttpEntity<>(ids);
            ResponseEntity<Map<Long, String>> exchange = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    setHttpEntity,
                    new ParameterizedTypeReference<Map<Long, String>>() {
                    }
            );
            return exchange.getBody();
        }catch (HttpClientErrorException e) {
        log.error("Ошибка при получении списка имён");
        return Collections.emptyMap();
    }catch (RestClientException e) {
        log.error("Внутрення ошибка", e);
        return Collections.emptyMap();
    }
    }

    public List<OrderDto> getUncheckedOrders(Integer size, Integer page) {
        String url = UriComponentsBuilder.fromUriString(properties.getOrderServiceUrl())
                .path("/order")
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


    public Optional<Integer> getScoreFromUserService(String param, boolean isByName) {
        String baseUrl;
        if (isByName) {
            baseUrl = properties.getUserServiceUrl()+"balance?name="+param;
        }else{
            baseUrl = properties.getUserServiceUrl()+"balance/"+param;
        }
        try {


            ResponseEntity<Integer> exchange = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }

            );
            return Optional.ofNullable(exchange.getBody());
        }catch (HttpClientErrorException e) {
            log.error("Ошибка при запросе очков {}", param, e);
            return Optional.empty();
        }catch (RestClientException e) {
            log.error("Внутрення ошибка", e);
            return Optional.empty();
        }
    }

    public List<ItemDto> getAllItems() {
        String url = properties.getOrderServiceUrl()+"/item";
        ResponseEntity<List<ItemDto>> exchange = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }

        );
        return exchange.getBody();
    }

}
