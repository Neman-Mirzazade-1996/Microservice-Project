package com.neman.userms.Service.ServiceImpl;

import com.neman.userms.Dto.auth.AuthenticationRequest;
import com.neman.userms.Dto.auth.AuthenticationResponse;
import com.neman.userms.Dto.auth.RegisterRequest;
import com.neman.userms.Exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAuthenticationService {

    @Value("${keycloak.auth-server-url:http://my-keycloak:8080}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:microservice-realm}")
    private String realm;

    @Value("${keycloak.client-id:microservice-client}")
    private String clientId;

    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin.password:admin}")
    private String adminPassword;

    private final WebClient webClient = WebClient.builder().build();

    public void register(RegisterRequest request) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm("master")
                    .username(adminUsername)
                    .password(adminPassword)
                    .clientId("admin-cli")
                    .build();

            UsersResource users = keycloak.realm(realm).users();

            // Check if user already exists
            List<UserRepresentation> existingUsers = users.search(request.getEmail());
            if (!existingUsers.isEmpty()) {
                throw new UserAlreadyExistException("User with this email already exists");
            }

            // Create user representation
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());

            // Create user
            users.create(user);
            log.info("User '{}' created successfully in Keycloak", request.getEmail());

            // Get the created user
            List<UserRepresentation> createdUsers = users.search(request.getEmail());
            if (!createdUsers.isEmpty()) {
                String userId = createdUsers.get(0).getId();

                // Set password
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(request.getPassword());
                credential.setTemporary(false);
                users.get(userId).resetPassword(credential);

                // Assign USER role
                RoleRepresentation userRole = keycloak.realm(realm).roles().get("USER").toRepresentation();
                users.get(userId).roles().realmLevel().add(Collections.singletonList(userRole));

                log.info("Password and USER role assigned to user '{}'", request.getEmail());
            }
        } catch (Exception e) {
            log.error("Error registering user '{}': {}", request.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Registration failed", e);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Prepare token request
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "password");
            formData.add("client_id", clientId);
            formData.add("username", request.getEmail());
            formData.add("password", request.getPassword());

            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            // Make token request to Keycloak
            Mono<Map> tokenResponse = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Map.class);

            Map<String, Object> tokens = tokenResponse.block();

            if (tokens == null || !tokens.containsKey("access_token")) {
                throw new RuntimeException("Authentication failed");
            }

            String accessToken = (String) tokens.get("access_token");
            String refreshToken = (String) tokens.get("refresh_token");

            log.info("User '{}' authenticated successfully", request.getEmail());

            return AuthenticationResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
            log.error("Authentication failed for user '{}': {}", request.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Authentication failed", e);
        }
    }
}