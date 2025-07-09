package com.neman.springcloud.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public List<GroupedOpenApi> apis() {
        return List.of(
                GroupedOpenApi.builder()
                        .group("user-service")
                        .pathsToMatch("/user-service/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("product-service")
                        .pathsToMatch("/product-service/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("order-service")
                        .pathsToMatch("/order-service/**")
                        .build()
        );
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Microservices API Documentation")
                        .description("Documentation for all microservices")
                        .version("1.0"));
    }
}
