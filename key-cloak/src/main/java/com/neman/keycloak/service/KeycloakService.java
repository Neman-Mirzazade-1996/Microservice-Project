package com.neman.keycloak.service;

import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);

    @Value("${keycloak.auth-server-url:http://localhost:8080}")
    private String keycloakServerUrl;

    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin.password:admin}")
    private String adminPassword;

    private static final String REALM_NAME = "microservice-realm";
    private static final String CLIENT_ID = "microservice-client";

    private Keycloak keycloak;

    @PostConstruct
    public void init() {
        logger.info("=== Keycloak Service Initialization Started ===");
        logger.info("Keycloak Server URL: {}", keycloakServerUrl);
        logger.info("Admin Username: {}", adminUsername);

        try {
            // Step 1: Initialize Keycloak admin client
            logger.info("Step 1: Creating Keycloak admin client connection...");
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm("master") // Düzəltdim: master123 -> master
                    .username(adminUsername)
                    .password(adminPassword)
                    .clientId("admin-cli")
                    .build();
            
            logger.info("Step 1: Keycloak admin client created successfully");

            // Step 2: Test connection by getting server info
            logger.info("Step 2: Testing connection to Keycloak server...");
            keycloak.serverInfo().getInfo(); // Bu, connection-u test edir
            logger.info("Step 2: Connection to Keycloak server successful");

            // Step 3: Setup realm and client
            logger.info("Step 3: Starting realm and client setup...");
            setupKeycloakRealm();
            logger.info("Step 3: Realm and client setup completed");

            logger.info("=== Keycloak Service Initialization Completed Successfully ===");

        } catch (jakarta.ws.rs.ProcessingException e) {
            logger.error("=== CONNECTION ERROR ===");
            logger.error("Failed to connect to Keycloak server at: {}", keycloakServerUrl);
            logger.error("Error details: {}", e.getMessage());
            logger.error("Possible causes:");
            logger.error("1. Keycloak server is not running");
            logger.error("2. Wrong server URL: {}", keycloakServerUrl);
            logger.error("3. Network connectivity issues");
            logger.error("Please check if Keycloak is running at: {}", keycloakServerUrl);

        } catch (jakarta.ws.rs.NotAuthorizedException e) {
            logger.error("=== AUTHENTICATION ERROR ===");
            logger.error("Failed to authenticate with Keycloak server");
            logger.error("Admin Username: {}", adminUsername);
            logger.error("Error details: {}", e.getMessage());
            logger.error("Possible causes:");
            logger.error("1. Wrong admin username: {}", adminUsername);
            logger.error("2. Wrong admin password");
            logger.error("3. Admin user does not exist in master realm");

        } catch (jakarta.ws.rs.NotFoundException e) {
            logger.error("=== REALM NOT FOUND ERROR ===");
            logger.error("Master realm not found or inaccessible");
            logger.error("Error details: {}", e.getMessage());
            logger.error("This usually means Keycloak is not properly initialized");

        } catch (Exception e) {
            logger.error("=== UNEXPECTED ERROR ===");
            logger.error("An unexpected error occurred during Keycloak initialization");
            logger.error("Error type: {}", e.getClass().getSimpleName());
            logger.error("Error message: {}", e.getMessage());
            logger.error("Full stack trace:", e);

        } finally {
            logger.info("=== Keycloak Service Initialization Process Finished ===");
        }
    }

    private void setupKeycloakRealm() {
        try {
            // Create realm if it doesn't exist
            createRealmIfNotExists();
            
            // Create client if it doesn't exist
            createClientIfNotExists();
            
            // Create roles
            createRoles();
            
            // Create test users
            createTestUsers();
            
        } catch (Exception e) {
            logger.error("Error setting up Keycloak realm: {}", e.getMessage(), e);
        }
    }

    private void createRealmIfNotExists() {
        try {
            keycloak.realm(REALM_NAME).toRepresentation();
            logger.info("Realm '{}' already exists", REALM_NAME);
        } catch (jakarta.ws.rs.NotFoundException e) {
            // Realm does not exist, create it
            logger.info("Creating realm '{}'", REALM_NAME);
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(REALM_NAME);
            realm.setDisplayName("Microservice Realm");
            realm.setEnabled(true);
            realm.setAccessTokenLifespan(3600); // 1 hour
            realm.setRefreshTokenMaxReuse(0);
            
            try {
                keycloak.realms().create(realm);
                logger.info("Realm '{}' created successfully", REALM_NAME);
            } catch (jakarta.ws.rs.ClientErrorException ce) {
                if (ce.getResponse().getStatus() == 409) {
                    // 409 Conflict means the realm already exists, which is fine
                    logger.info("Realm '{}' already exists (409 Conflict)", REALM_NAME);
                } else {
                    // Some other client error
                    logger.error("Error creating realm '{}': {}", REALM_NAME, ce.getMessage());
                    throw ce;
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected error checking/creating realm '{}': {}", REALM_NAME, e.getMessage());
            throw e;
        }
    }

    private void createClientIfNotExists() {
        RealmResource realm = keycloak.realm(REALM_NAME);
        
        // Client-in mövcud olub-olmadığını yoxlamaq üçün ID ilə axtarış edirik
        List<ClientRepresentation> clients = realm.clients().findByClientId(CLIENT_ID);

        // Əgər qayıdan siyahı boşdursa, bu o deməkdir ki, client mövcud deyil
        if (clients.isEmpty()) {
            logger.info("Creating client '{}'", CLIENT_ID);
            
            ClientRepresentation client = new ClientRepresentation();
            client.setClientId(CLIENT_ID);
            client.setName("Microservice Client");
            client.setDescription("Client for microservice authentication");
            client.setEnabled(true);
            client.setPublicClient(true);
            client.setDirectAccessGrantsEnabled(true);
            client.setStandardFlowEnabled(true);
            client.setImplicitFlowEnabled(false);
            client.setServiceAccountsEnabled(false);
            client.setRedirectUris(Arrays.asList("*"));
            client.setWebOrigins(Arrays.asList("*"));
            
            realm.clients().create(client);
            logger.info("Client '{}' created successfully", CLIENT_ID);
        } else {
            // Əgər siyahı boş deyilsə, deməli client artıq mövcuddur
            logger.info("Client '{}' already exists", CLIENT_ID);
        }
    }

    private void createRoles() {
        RealmResource realm = keycloak.realm(REALM_NAME);
        
        // Create roles
        String[] roles = {"ADMIN", "USER", "MANAGER"};
        
        for (String roleName : roles) {
            try {
                realm.roles().get(roleName).toRepresentation();
                logger.info("Role '{}' already exists", roleName);
            } catch (Exception e) {
                RoleRepresentation role = new RoleRepresentation();
                role.setName(roleName);
                role.setDescription("Role for " + roleName.toLowerCase() + " access");
                realm.roles().create(role);
                logger.info("Role '{}' created successfully", roleName);
            }
        }
    }

    private void createTestUsers() {
        RealmResource realm = keycloak.realm(REALM_NAME);
        UsersResource users = realm.users();
        
        // Create admin user
        createUserIfNotExists(users, "admin", "admin@example.com", "admin123", "ADMIN");
        
        // Create regular user
        createUserIfNotExists(users, "user", "user@example.com", "user123", "USER");
        
        // Create manager user
        createUserIfNotExists(users, "manager", "manager@example.com", "manager123", "MANAGER");
    }

    private void createUserIfNotExists(UsersResource users, String username, String email, String password, String roleName) {
        try {
            List<UserRepresentation> existingUsers = users.search(username);
            if (!existingUsers.isEmpty()) {
                logger.info("User '{}' already exists", username);
                return;
            }

            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setFirstName(username.substring(0, 1).toUpperCase() + username.substring(1));
            user.setLastName("Test");

            // Create user
            users.create(user);
            logger.info("User '{}' created successfully", username);

            // Get the created user
            List<UserRepresentation> createdUsers = users.search(username);
            if (!createdUsers.isEmpty()) {
                String userId = createdUsers.get(0).getId();
                
                // Set password
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(password);
                credential.setTemporary(false);
                users.get(userId).resetPassword(credential);
                
                // Assign role
                RoleRepresentation role = keycloak.realm(REALM_NAME).roles().get(roleName).toRepresentation();
                users.get(userId).roles().realmLevel().add(Collections.singletonList(role));
                
                logger.info("Password and role '{}' assigned to user '{}'", roleName, username);
            }
        } catch (Exception e) {
            logger.error("Error creating user '{}': {}", username, e.getMessage(), e);
        }
    }
}