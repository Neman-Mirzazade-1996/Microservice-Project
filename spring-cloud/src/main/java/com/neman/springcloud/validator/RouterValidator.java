package com.neman.springcloud.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/user-service/api/v1/auth/register",
            "/user-service/api/v1/auth/login",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri ->
                            request.getURI().getPath().equals(uri) ||
                                    (uri.endsWith("/**") && request.getURI().getPath().startsWith(uri.substring(0, uri.length() - 3)))
                    );
}
