# Keycloak Integration Guide

## Overview

This microservice project now uses Keycloak for centralized authentication and authorization. Role-based permissions are managed through Keycloak's admin interface instead of hardcoded rules in the gateway.

## Architecture

```
User → User Service → Keycloak → JWT Token
Client → Nginx → Spring Cloud Gateway (validates Keycloak JWT) → Microservice
```

## Authentication Flow

1. **User Registration**: POST `/api/v1/users/auth/register` creates user in Keycloak
2. **User Login**: POST `/api/v1/users/auth/login` authenticates against Keycloak and returns JWT
3. **API Access**: Include JWT in Authorization header for secured endpoints

## Keycloak Configuration

### Realm: `microservice-realm`
### Client: `microservice-client`
### Roles:
- `ADMIN`: Full access to all endpoints
- `USER`: Basic user access
- `MANAGER`: Management access

### Test Users (automatically created):
- **admin**: admin@example.com / admin123 (ADMIN role)
- **user**: user@example.com / user123 (USER role)  
- **manager**: manager@example.com / manager123 (MANAGER role)

## API Endpoints

### Public Endpoints (no authentication required)
- `POST /api/v1/users/auth/register` - Register new user
- `POST /api/v1/users/auth/login` - Login user
- `GET /health` - Health check

### Secured Endpoints (JWT required)
- `GET /api/v1/users/*` - User operations
- `GET /api/v1/products/*` - Product operations (read)
- `POST/PUT/DELETE /api/v1/products/*` - Product operations (ADMIN only)
- `GET /api/v1/orders/*` - Order operations

## Usage Examples

### 1. Register a new user
```bash
curl -X POST http://localhost/api/v1/users/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe", 
    "email": "john.doe@example.com",
    "password": "password123",
    "phone": "1234567890",
    "address": "123 Main St",
    "city": "New York",
    "country": "USA",
    "postalCode": "10001"
  }'
```

### 2. Login and get JWT token
```bash
curl -X POST http://localhost/api/v1/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123"
  }'
```

### 3. Access secured endpoint
```bash
curl -X GET http://localhost/api/v1/users/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Keycloak Admin Console

Access Keycloak admin console at: http://localhost:8080/auth/admin
- Username: admin
- Password: admin

## Services

- **Keycloak**: http://localhost:8080 - Identity provider
- **Gateway**: http://localhost:8080 - API Gateway with authorization
- **User Service**: http://localhost:4020 - User management
- **Product Service**: http://localhost:4010 - Product catalog
- **Order Service**: http://localhost:4030 - Order processing
- **Nginx**: http://localhost:80 - Load balancer

## Deployment

```bash
docker-compose up --build
```

Wait for all services to be healthy, then test the authentication flow.

## Role Management

Roles can now be managed through Keycloak Admin Console:
1. Go to Keycloak Admin Console
2. Select `microservice-realm`
3. Go to Roles → Realm Roles
4. Add/edit/delete roles as needed
5. Assign roles to users in Users → [Select User] → Role Mapping

No code changes are required for role modifications!