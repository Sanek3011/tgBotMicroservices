package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.events.UserRoleEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class RoleSyncStartService {

    private final RestTemplate restTemplate;
    private final TelegramBaseService service;

    public RoleSyncStartService(@Qualifier("userService") RestTemplate restTemplate, TelegramBaseService service) {
        this.restTemplate = restTemplate;
        this.service = service;
    }

    public void syncRoles() {
        ResponseEntity<List<UserRoleEvent>> exchange = restTemplate.exchange(
                "/roles/non-guest",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        List<UserRoleEvent> body = exchange.getBody();
        service.saveAll(body);
    }

    @Scheduled(fixedDelay = 300_000)
    public void scheduleSynchronizer() {
        try {
            syncRoles();
        } catch (RestClientException e) {
            log.error("Не удалось обновить", e);
        }
    }
}
