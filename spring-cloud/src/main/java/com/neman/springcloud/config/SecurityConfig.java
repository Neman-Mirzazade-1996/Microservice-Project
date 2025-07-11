package com.neman.springcloud.config;

import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${spring.application.security.jwt.secret-key}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                    .pathMatchers(
                            "/user-service/api/v1/auth/**",
                            "/swagger-ui/**",
                            "/swagger-ui/index.html",
                            "/webjars/**",
                            "/swagger-resources/**",
                            "/v3/api-docs",
                            "/v3/api-docs/**",
                            "/v3/api-docs/swagger-config",
                            "/swagger-ui.html",
                            "/*/v3/api-docs",
                            "/*/swagger-ui.html",
                            "/*/swagger-ui/**",
                            "/actuator/**",
                            "/user-service/v3/api-docs/**",
                            "/product-service/v3/api-docs/**",
                            "/order-service/v3/api-docs/**"
                    ).permitAll()

                        // Admin only endpoints - Product Management
                        .pathMatchers(HttpMethod.POST, "/product-service/api/v1/products/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/product-service/api/v1/products/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/product-service/api/v1/products/admin/**").hasAuthority("ROLE_ADMIN")

                        // Additional rules - match all product APIs requiring admin
                        .pathMatchers(HttpMethod.POST, "/product-service/api/v1/products/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/product-service/api/v1/products/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/product-service/api/v1/products/**").hasAuthority("ROLE_ADMIN")

                        // Admin only endpoints - Category
                        .pathMatchers(HttpMethod.POST, "/product-service/api/v1/categories/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/product-service/api/v1/categories/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/product-service/api/v1/categories/admin/**").hasAuthority("ROLE_ADMIN")

                    .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> {
                        jwt.jwtDecoder(jwtDecoder());
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                    })
                );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        try {
            log.debug("Using JWT secret key for decoding: {}", jwtSecret);

            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

            return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
        } catch (Exception e) {
            log.error("Failed to create JWT decoder: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create JWT decoder", e);
        }
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();

        // Configure authorities converter to extract roles from the right claim
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix(""); // No prefix as "ROLE_" is already in the claim

        converter.setJwtGrantedAuthoritiesConverter(jwt ->
            Mono.fromCallable(() -> authoritiesConverter.convert(jwt))
                .flatMapMany(authorities -> Flux.fromIterable(authorities))
                .doOnNext(authority -> log.debug("Extracted authority: {}", authority))
        );

        return converter;
    }
}
