package com.neman.springcloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    private final ReactiveJwtConverter reactiveJwtConverter;

    public SecurityConfig(ReactiveJwtConverter reactiveJwtConverter) {
        this.reactiveJwtConverter = reactiveJwtConverter;
    }

    @Bean
    @Lazy
    public ReactiveJwtDecoder jwtDecoder() {
        try {
            return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
        } catch (Exception e) {
            log.warn("Failed to create JwtDecoder from issuer location: {}. Will retry later.", issuerUri, e);
            // Return a fallback decoder that will retry
            return createRetryableJwtDecoder();
        }
    }

    private ReactiveJwtDecoder createRetryableJwtDecoder() {
        return jwt -> {
            try {
                ReactiveJwtDecoder decoder = ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
                return decoder.decode(jwt);
            } catch (Exception e) {
                log.error("Failed to decode JWT token", e);
                return Mono.error(new IllegalArgumentException("Unable to decode JWT token", e));
            }
        };
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOriginPatterns(Collections.singletonList("*"));
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(Arrays.asList("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .authorizeExchange(exchanges -> exchanges
                        // İctimai endpoint-lər (authentication tələb olunmur)
                        .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                        .pathMatchers("/login/**", "/oauth2/**").permitAll()
                        .pathMatchers("/auth/**").permitAll() // Keycloak endpoints
                        .pathMatchers("/realms/**").permitAll() // Keycloak realms

                        // API documentation
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // User service endpoint-ləri
                        .pathMatchers(HttpMethod.POST, "/api/v1/users/signup").hasAuthority("ROLE_role_admin")
                        .pathMatchers(HttpMethod.GET, "/api/v1/users/search").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")
                        .pathMatchers(HttpMethod.GET, "/api/v1/users/findByUsername/**").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")
                        .pathMatchers("/api/v1/users/**").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")

                        // Product service endpoint-ləri
                        .pathMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll() // Public read access
                        .pathMatchers("/api/v1/products/**").hasAnyAuthority("ROLE_role_admin")

                        // Order service endpoint-ləri
                        .pathMatchers("/api/v1/orders/**").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")

                        // Payment service endpoint-ləri
                        .pathMatchers("/api/v1/payments/**").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")

                        // Inventory service endpoint-ləri
                        .pathMatchers("/api/v1/inventory/**").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")

                        // Digər bütün sorğular üçün autentifikasiya tələb olunur
                        .anyExchange()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder())
                                .jwtAuthenticationConverter(reactiveJwtConverter)
                        )
                );

        return http.build();
    }
}
