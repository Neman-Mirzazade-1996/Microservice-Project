package com.neman.springcloud.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/user-service/api/v1/users/auth/register",
            "/user-service/api/v1/users/auth/login",
            "/user-service/auth/register",
            "/user-service/auth/login",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/actuator/**",
            "/user-service/v3/api-docs/**",
            "/product-service/v3/api-docs/**",
            "/order-service/v3/api-docs/**"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
