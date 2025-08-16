package com.neman.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient userServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://user-service:4020") // Docker container adı
                .build();
    }
}
