package com.neman.userms.Controller;

import com.neman.userms.Dto.auth.AuthenticationRequest;
import com.neman.userms.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication APIs for internal use")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Get user info by email and password (for internal use by gateway)")
    public ResponseEntity<Map<String, Object>> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        try {
            // Istifadəçini email ilə tap (User entity qaytarır)
            var user = userService.findUserEntityByEmail(request.getEmail());

            // Password yoxlaması spring-cloud-da həyata keçiriləcək
            // Burada sadəcə user məlumatlarını qaytırırıq

            // Istifadəçi məlumatlarını qaytır
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("email", user.getEmail());
            userInfo.put("firstName", user.getFirstName());
            userInfo.put("lastName", user.getLastName());
            userInfo.put("role", user.getRole().toString());
            userInfo.put("password", user.getPassword()); // Spring-cloud password yoxlaması üçün

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
