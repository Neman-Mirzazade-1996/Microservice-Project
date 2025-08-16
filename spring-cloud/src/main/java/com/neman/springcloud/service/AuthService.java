package com.neman.springcloud.service;

import com.neman.springcloud.dto.AuthResponse;
import com.neman.springcloud.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient userServiceWebClient;
    private final JwtService jwtService;

    public Mono<AuthResponse> authenticate(LoginRequest loginRequest) {
        log.info("Authenticating user: {}", loginRequest.getEmail());

        // User service-dən istifadəçi məlumatlarını al və parolunu yoxla
        return userServiceWebClient
                .post()
                .uri("/api/v1/users/auth/login")
                .bodyValue(loginRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(userResponse -> {
                    log.info("User authenticated successfully: {}", loginRequest.getEmail());

                    // Password-u yoxla
                    String storedPassword = (String) userResponse.get("password");
                    if (storedPassword == null || !storedPassword.equals(loginRequest.getPassword())) {
                        return Mono.error(new RuntimeException("Invalid password"));
                    }

                    // JWT token yarad
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("userId", userResponse.get("id"));
                    claims.put("email", userResponse.get("email"));
                    claims.put("role", userResponse.get("role"));
                    claims.put("firstName", userResponse.get("firstName"));
                    claims.put("lastName", userResponse.get("lastName"));

                    String accessToken = jwtService.generateToken(claims, (String) userResponse.get("email"));
                    String refreshToken = jwtService.generateRefreshToken((String) userResponse.get("email"));

                    AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                            .id(Long.valueOf(userResponse.get("id").toString()))
                            .email((String) userResponse.get("email"))
                            .firstName((String) userResponse.get("firstName"))
                            .lastName((String) userResponse.get("lastName"))
                            .role((String) userResponse.get("role"))
                            .build();

                    return Mono.just(AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .tokenType("Bearer")
                            .expiresIn(3600L) // 1 saat
                            .user(userInfo)
                            .build());
                })
                .doOnError(error -> log.error("Authentication failed for user: {}", loginRequest.getEmail(), error));
    }

    public Mono<Boolean> validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            if (username == null) {
                return Mono.just(false);
            }

            return jwtService.isTokenValidReactive(token, username);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return Mono.just(false);
        }
    }
}
