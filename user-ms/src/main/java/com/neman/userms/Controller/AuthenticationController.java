package com.neman.userms.Controller;

import com.neman.userms.Dto.auth.AuthenticationRequest;
import com.neman.userms.Dto.auth.AuthenticationResponse;
import com.neman.userms.Dto.auth.RegisterRequest;
import com.neman.userms.Service.ServiceImpl.KeycloakAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private final KeycloakAuthenticationService keycloakAuthService;

    @PostMapping("/register")
    @Operation(summary = "Register new user in Keycloak")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        keycloakAuthService.register(request);
        return ResponseEntity.ok("User registered successfully in Keycloak");
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get Keycloak token")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(keycloakAuthService.authenticate(request));
    }
}
