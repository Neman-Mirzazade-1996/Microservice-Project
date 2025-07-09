package com.neman.springcloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.application.security.jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                    .pathMatchers(
                        "/user-service/auth/**",
                        "/swagger-ui/**",
                        "/swagger-ui/index.html",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/swagger-resources/**"
                    ).permitAll()
                        // Admin only endpoints - Product
                        .pathMatchers(HttpMethod.POST, "/product-service/admin/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/product-service/admin/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/product-service/admin/**").hasRole("ADMIN")

                        // Admin only endpoints - Category
                        .pathMatchers(HttpMethod.POST, "/api/v1/categories/admin/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/categories/admin/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/categories/admin/**").hasRole("ADMIN")
                    .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> {})
                );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] key = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec spec = new SecretKeySpec(key, "HMAC");
        return NimbusReactiveJwtDecoder.withSecretKey(spec).build();
    }
}
