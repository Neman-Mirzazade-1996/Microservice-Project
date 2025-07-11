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

    @Value("${keycloak.auth-server-url:http://my-keycloak:8080}")
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
        try {
            logger.info("Initializing Keycloak admin client...");
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm("master")
                    .username(adminUsername)
                    .password(adminPassword)
                    .clientId("admin-cli")
                    .build();
            
            // Setup realm and client
            setupKeycloakRealm();
            logger.info("Keycloak setup completed successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize Keycloak: {}", e.getMessage(), e);
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
        } catch (Exception e) {
            logger.info("Creating realm '{}'", REALM_NAME);
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(REALM_NAME);
            realm.setDisplayName("Microservice Realm");
            realm.setEnabled(true);
            realm.setAccessTokenLifespan(3600); // 1 hour
            realm.setRefreshTokenMaxReuse(0);
            
            keycloak.realms().create(realm);
            logger.info("Realm '{}' created successfully", REALM_NAME);
        }
    }

    private void createClientIfNotExists() {
        RealmResource realm = keycloak.realm(REALM_NAME);
        
        try {
            realm.clients().findByClientId(CLIENT_ID);
            logger.info("Client '{}' already exists", CLIENT_ID);
        } catch (Exception e) {
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