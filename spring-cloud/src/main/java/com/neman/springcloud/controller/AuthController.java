package com.neman.springcloud.controller;

import com.neman.springcloud.dto.AuthResponse;
import com.neman.springcloud.dto.LoginRequest;
import com.neman.springcloud.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());

        return authService.authenticate(loginRequest)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @PostMapping("/validate")
    public Mono<ResponseEntity<Boolean>> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.just(ResponseEntity.badRequest().body(false));
        }

        String token = authHeader.substring(7);
        return authService.validateToken(token)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<String>> logout() {
        // JWT tokenları stateless olduğu üçün, logout client-də token-ı silməklə həyata keçirilir
        return Mono.just(ResponseEntity.ok("Logged out successfully"));
    }
}
