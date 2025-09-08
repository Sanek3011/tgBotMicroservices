package org.example.service.output;

import org.example.entity.dto.IdRequestDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Service
public class LoginOutputService {
    private final RestTemplate restTemplate;


    public LoginOutputService(@Qualifier("userService") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    public String getOneTimeLoginUrl(Long tgId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("999d6327-1b80-4ce4-8e15-b51a21ec912d");
        ResponseEntity<String> exchange = restTemplate.exchange(
                "/login",
                HttpMethod.POST,
                new HttpEntity<>(new IdRequestDto(tgId)),
                new ParameterizedTypeReference<>() {
                }
        );
        return exchange.getBody();
    }
}
