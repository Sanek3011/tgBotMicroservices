package org.example.bff.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver;

@Configuration
@EnableAutoConfiguration
public class AppConfig {

    @Bean
    public ViewResolver viewResolver() {
        return new ThymeleafReactiveViewResolver();
    }

}
