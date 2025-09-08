package org.example.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Configuration
public class RestTemplateConfig {

    private final String serviceToken;
    private final String userServiceUrl;
    private final String orderServiceUrl;
    private final String reportServiceUrl;

    public RestTemplateConfig(@Value("${services-rest.order-service-url}") String orderServiceUrl,
                              @Value("${service-token}") String serviceToken,
                              @Value("${services-rest.user-service-url}") String userServiceUrl,
                              @Value("${services-rest.report-service-url}") String reportServiceUrl) {
        this.orderServiceUrl = orderServiceUrl;
        this.serviceToken = serviceToken;
        this.userServiceUrl = userServiceUrl;
        this.reportServiceUrl = reportServiceUrl;
    }

    @Bean
    @Qualifier("userService")
    public RestTemplate restTemplateUser(RestTemplateBuilder builder) {
        return builder.rootUri(userServiceUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + serviceToken)
                .build();
    }

    @Bean
    @Qualifier("reportService")
    public RestTemplate restTemplateReport(RestTemplateBuilder builder) {
        return builder.rootUri(reportServiceUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + serviceToken)
                .build();
    }

    @Bean
    @Qualifier("orderService")
    public RestTemplate restTemplateOrder(RestTemplateBuilder builder) {
        return builder.rootUri(orderServiceUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + serviceToken)
                .build();
    }


}
