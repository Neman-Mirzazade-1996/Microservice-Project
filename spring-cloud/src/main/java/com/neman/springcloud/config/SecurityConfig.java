package com.neman.springcloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // İctimai endpoint-lər (authentication tələb olunmur)
                        .pathMatchers("/actuator/health", "/api/v1/auth/**")
                        .permitAll()

                        // User service endpoint-ləri (authentication tələb olunur)
                        .pathMatchers(HttpMethod.POST, "/api/v1/users/signup").hasAuthority("ROLE_role_admin")
                        .pathMatchers(HttpMethod.GET, "/api/v1/users/search").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")
                        .pathMatchers(HttpMethod.GET, "/api/v1/users/findByUsername/**").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")
                        .pathMatchers("/api/v1/users/**").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")

                        // Product service endpoint-ləri
                        .pathMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .pathMatchers("/api/v1/products/**").hasAnyAuthority("ROLE_role_admin")

                        // Order service endpoint-ləri
                        .pathMatchers("/api/v1/orders/**").hasAnyAuthority("ROLE_role_admin", "ROLE_role_user")

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
