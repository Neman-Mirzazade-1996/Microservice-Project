package com.neman.keycloak.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/test")
    public ResponseEntity<String> test(JwtAuthenticationToken token) {
        return ResponseEntity.ok("Hello, " + token.getName());
    }

}
