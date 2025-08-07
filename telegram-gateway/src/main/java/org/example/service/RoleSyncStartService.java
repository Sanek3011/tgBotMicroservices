package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.kafka.events.UserRoleEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleSyncStartService {

    private final RestTemplate restTemplate;
    private final TelegramBaseService service;
    @Value("${services-rest.user-service-url}")
    private String roleURL;

    public void syncRoles() {
        ResponseEntity<List<UserRoleEvent>> exchange = restTemplate.exchange(
                roleURL+"roles/non-guest",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        List<UserRoleEvent> body = exchange.getBody();
        service.saveAll(body);
    }
}
