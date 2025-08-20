# Mikroservis Keycloak Security Setup Guide

## 1. Sistemin İşə Salınması

### Addım 1: Docker Container-ləri başlatın
```bash
# Root qovluqda (docker-compose.yml faylının olduğu yerdə)
docker-compose up -d
```

Bu komanda aşağıdakı servisleri işə salacaq:
- PostgreSQL (Keycloak üçün database)
- Keycloak Server (8080 portunda)
- MySQL databases (user, order, product servislər üçün)
- Mikroservislər (user-service, product-service, order-service)
- Spring Cloud Gateway (8085 portunda)
- Nginx Load Balancer (80 portunda)

### Addım 2: Keycloak Server-in işləyib-işləmədiyini yoxlayın
```bash
# Health check
curl http://localhost:8080/health/ready

# Browser-də:
http://localhost:8080
```

## 2. Keycloak Admin Console Konfiqurasiyası

### Addım 1: Admin Console-a daxil olun
- URL: http://localhost:8080
- Username: `admin`
- Password: `admin123`

### Addım 2: Realm yaradın
1. Sol üst küncdə "Master" dropdown-dan "Create Realm" seçin
2. Realm name: `demo-app`
3. "Create" düyməsini basın

### Addım 3: Client yaradın
1. Sol menyudan "Clients" seçin
2. "Create client" düyməsini basın
3. Konfiqurasiya:
   - Client ID: `demo`
   - Client type: `OpenID Connect`
   - "Next" düyməsini basın
4. Capability config:
   - Client authentication: `ON`
   - Authorization: `OFF` 
   - Standard flow: `ON`
   - Direct access grants: `ON`
   - "Next" düyməsini basın
5. Login settings:
   - Valid redirect URIs: `http://localhost:8085/*`
   - Valid post logout redirect URIs: `http://localhost:8085/*`
   - Web origins: `http://localhost:8085`
   - "Save" düyməsini basın

### Addım 4: Client Secret alın
1. Yaratdığınız `demo` client-ə daxil olun
2. "Credentials" tab-ına keçin
3. Client secret-i kopyalayın və application.yml-də yoxlayın

### Addım 5: Realm Roles yaradın
1. Sol menyudan "Realm roles" seçin
2. "Create role" düyməsini basın
3. İki role yaradın:
   - Role name: `role_admin`
   - Role name: `role_user`

### Addım 6: Test istifadəçiləri yaradın
1. Sol menyudan "Users" seçin
2. "Add user" düyməsini basın

**Admin istifadəçisi:**
- Username: `admin`
- Email: `admin@demo.com`
- First name: `Admin`
- Last name: `User`
- Email verified: `ON`
- "Create" düyməsini basın

**Credentials tab-ında:**
- Password: `admin123`
- Password temporary: `OFF`
- "Set password" düyməsini basın

**Role mappings tab-ında:**
- "Assign role" düyməsini basın
- `role_admin` seçin və assign edin

**Regular istifadəçisi:**
- Username: `user`
- Email: `user@demo.com`
- First name: `Regular`
- Last name: `User`
- Password: `user123`
- Role: `role_user`

## 3. Mikroservislərin Test Edilməsi

### Gateway Health Check
```bash
curl http://localhost:8085/actuator/health
```

### Authentication Test
```bash
# 1. OAuth2 login flow
curl -X GET "http://localhost:8085/oauth2/authorization/keycloak"

# 2. User info (JWT token lazımdır)
curl -X GET "http://localhost:8085/api/v1/auth/user-info" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"

# 3. Health check
curl http://localhost:8085/api/v1/auth/health
```

### Protected Endpoints Test
```bash
# Admin-only endpoint (user signup)
curl -X POST "http://localhost:8085/api/v1/users/signup" \
     -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"username":"newuser","email":"new@test.com"}'

# Public endpoint (product list)
curl -X GET "http://localhost:8085/api/v1/products"

# User/Admin endpoint (orders)
curl -X GET "http://localhost:8085/api/v1/orders" \
     -H "Authorization: Bearer USER_OR_ADMIN_JWT_TOKEN"
```

## 4. JWT Token Alma

### Postman və ya curl ilə:
```bash
curl -X POST "http://localhost:8080/realms/demo-app/protocol/openid-connect/token" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "grant_type=password" \
     -d "client_id=demo" \
     -d "client_secret=lFacC7i5yw2l0KwQoycx9WuJryxT0zUu" \
     -d "username=admin" \
     -d "password=admin123"
```

## 5. Mikroservis Endpoints-ləri

### Public Endpoints (Authentication tələb olunmur):
- `GET /api/v1/products/**` - Product məlumatları
- `GET /actuator/health` - Health check
- `GET /api/v1/auth/health` - Keycloak connection status

### Admin Only Endpoints:
- `POST /api/v1/users/signup` - Yeni istifadəçi yaratmaq
- `POST/PUT/DELETE /api/v1/products/**` - Product management

### User + Admin Endpoints:
- `GET /api/v1/users/**` - User məlumatları
- `GET/POST /api/v1/orders/**` - Order operations

## 6. Frontend Integration

Frontend tətbiqlər üçün:
1. Login URL: `http://localhost:8085/oauth2/authorization/keycloak`
2. JWT token-ları `Authorization: Bearer <token>` header-də göndərin
3. Token refresh üçün refresh_token istifadə edin

## 7. Troubleshooting

### Ümumi problemlər:
1. **Keycloak connection error**: 
   ```bash
   docker logs my-keycloak
   ```
2. **Database connection issues**:
   ```bash
   docker logs postgres
   ```
3. **Gateway routing issues**:
   ```bash
   docker logs spring-cloud-gateway
   ```

### Debug logs:
```bash
# Gateway logs
docker logs -f spring-cloud-gateway

# Keycloak logs  
docker logs -f my-keycloak
```

## 8. Əlavə Konfiqurasiyalar

### Production üçün:
1. Environment variables istifadə edin
2. HTTPS konfiqurasiya edin
3. Rate limiting əlavə edin
4. Token expiration time-ları optimallaşdırın

### Service discovery üçün:
Mikroservislər arasında service discovery istifadə etmək üçün Eureka və ya Consul əlavə edə bilərsiniz.
