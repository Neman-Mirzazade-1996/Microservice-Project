package com.neman.springcloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

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
                            "/order-service/v3/api-docs/**",
                            "/auth/**",
                            "/health/**"
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
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter()))
                );
        return http.build();
    }

    @Bean
    public Converter<Jwt, Mono<org.springframework.security.authentication.AbstractAuthenticationToken>> keycloakJwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            log.debug("Processing JWT with claims: {}", jwt.getClaims());
            
            // Extract realm roles from Keycloak JWT
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            Collection<String> roles = null;
            
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                roles = (Collection<String>) realmAccess.get("roles");
            }
            
            if (roles == null || roles.isEmpty()) {
                log.debug("No realm roles found in JWT");
                return Flux.empty();
            }
            
            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> {
                        String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
                        log.debug("Mapped role '{}' to authority '{}'", role, authority);
                        return new SimpleGrantedAuthority(authority);
                    })
                    .collect(Collectors.toList());
            
            log.debug("Extracted authorities: {}", authorities);
            return Flux.fromIterable(authorities);
        });
        
        return converter;
    }
}
