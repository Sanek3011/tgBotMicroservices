package org.example.bff.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    private final String serviceToken;
    private final String userServiceUrl;
    private final String orderServiceUrl;
    private final String reportServiceUrl;

    public WebClientConfiguration(@Value("${services.order-service.url}") String orderServiceUrl,
                                  @Value("${services.user-service.url}") String userServiceUrl,
                                  @Value("${services.report-service.url}") String reportServiceUrl,
                                  @Value("${services.service-token}") String serviceToken) {
        this.orderServiceUrl = orderServiceUrl;
        this.userServiceUrl = userServiceUrl;
        this.reportServiceUrl = reportServiceUrl;
        this.serviceToken = serviceToken;
    }

    @Bean
    @Qualifier("userService")
    public WebClient webClientUser() {
        return webClient(userServiceUrl);
    }

    @Bean
    @Qualifier("orderService")
    public WebClient webClientOrder() {
        return webClient(orderServiceUrl);
    }

    @Bean
    @Qualifier("reportService")
    public WebClient webClientReport() {
        return webClient(reportServiceUrl);
    }

    private WebClient webClient(String url) {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer "+serviceToken)
                .build();
    }
}
