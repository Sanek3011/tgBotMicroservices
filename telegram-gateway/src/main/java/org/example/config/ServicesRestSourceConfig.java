package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services-rest")
@Data
public class ServicesRestSourceConfig {

    private String userServiceUrl;
    private String orderServiceUrl;
}
