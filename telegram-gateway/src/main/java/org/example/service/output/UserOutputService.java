package org.example.service.output;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.dto.UserDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
public class UserOutputService {

    private final RestTemplate restTemplate;


    public UserOutputService(@Qualifier("userService") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    public Long getCountUsers() {
        return restTemplate.getForObject("/users/count", Long.class);
    }

    public List<UserDto> getAllUsers() {
        try {
            ResponseEntity<List<UserDto>> exchange = restTemplate.exchange(
                    "/users/tg",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<UserDto>>() {
                    }
            );
            return exchange.getBody();
        }catch (RestClientException e) {
            return List.of();
        }
    }

    public Map<Long, String> getNamesById(Set<Long> ids) {
        try {
            HttpEntity<Set<Long>> setHttpEntity = new HttpEntity<>(ids);
            ResponseEntity<Map<Long, String>> exchange = restTemplate.exchange(
                    "/users/names",
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
    public Optional<Integer> getScoreFromUserService(String param, boolean isByName) {
        String baseUrl;
        if (isByName) {
            baseUrl = "balance?name="+param;
        }else{
            baseUrl = "balance/"+param;
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
}
