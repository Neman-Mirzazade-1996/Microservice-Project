# Microservices Application

A modern microservices architecture built with Spring Boot, showcasing service orchestration, load balancing, and containerization.

## 📋 Table of Contents
- [Architecture Overview](#architecture-overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Service Details](#service-details)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Monitoring & Health](#monitoring--health)
- [Troubleshooting](#troubleshooting)

## 🏗 Architecture Overview

This project implements a microservices architecture with three core services:

- **User Service** (Port: 4020) - Manages user data and authentication
- **Product Service** (Port: 4010) - Handles product catalog and inventory
- **Order Service** (Port: 4030) - Processes orders and orchestrates service communication

### Key Features

- **Load Balancing**: Nginx reverse proxy for intelligent request distribution
- **Service Discovery**: Docker DNS-based service resolution
- **Database per Service**: Independent MySQL instance for each service
- **Resilient Communication**: Feign clients with circuit breaking
- **Containerization**: Docker-based deployment
- **Health Monitoring**: Actuator endpoints for service health

## 💻 Technology Stack

- **Framework**: Spring Boot 3.x
- **Java Version**: JDK 21
- **Build Tool**: Gradle 8.x
- **Database**: MySQL 8.0
- **Service Communication**: Spring Cloud OpenFeign
- **Load Balancer**: Nginx
- **Containerization**: Docker & Docker Compose
- **Documentation**: SpringDoc OpenAPI 3.0
- **Monitoring**: Spring Boot Actuator

## 📂 Project Structure

\`\`\`
microservice-app-main/
├── user-ms/               # User management service
│   ├── src/              # Service source code
│   ├── docker/           # Docker configuration
│   └── build.gradle      # Service build config
├── product-ms/           # Product catalog service
│   ├── src/
│   ├── docker/
│   └── build.gradle
├── order-ms/             # Order processing service
│   ├── src/
│   ├── docker/
│   └── build.gradle
├── nginx/                # Load balancer configuration
│   └── nginx.conf       # Nginx routing rules
├── docker-compose.yml    # Service orchestration
└── build.gradle         # Root build configuration
\`\`\`

## 🔧 Prerequisites

- JDK 21
- Docker & Docker Compose
- Gradle 8.x
- MySQL 8.0 (for local development)

## 🚀 Getting Started

1. **Clone the Repository**
   \`\`\`bash
   git clone <repository-url>
   cd microservice-app-main
   \`\`\`

2. **Build Services**
   \`\`\`bash
   ./gradlew clean build
   \`\`\`

3. **Start Infrastructure**
   \`\`\`bash
   docker-compose up --build
   \`\`\`

4. **Verify Services**
   ```bash
   curl http://localhost/health/user
   curl http://localhost/health/product
   curl http://localhost/health/order
   ```

## 🔍 Service Details

### User Service
- **Port**: 4020
- **Base URL**: /api/v1/users
- **Database**: user_db
- **Key Functions**: User management, authentication

### Product Service
- **Port**: 4010
- **Base URL**: /api/v1/products
- **Database**: product_db
- **Key Functions**: Product catalog, inventory management

### Order Service
- **Port**: 4030
- **Base URL**: /api/v1/orders
- **Database**: order_db
- **Key Functions**: Order processing, service orchestration

## 📚 API Documentation

### User Service Endpoints
- `POST /api/v1/users/createUser` - Create new user
- `GET /api/v1/users/getUser/{id}` - Get user details
- `GET /api/v1/users/health` - Service health check

### Product Service Endpoints
- `POST /api/v1/products/create` - Create product
- `GET /api/v1/products/get/{id}` - Get product details
- `PUT /api/v1/products/decreaseStock/{productId}/{quantity}` - Update stock
- `GET /api/v1/products/health` - Service health check

### Order Service Endpoints
- `POST /api/v1/orders/create` - Create order
- `GET /api/v1/orders/getOrderById/{id}` - Get order details
- `GET /api/v1/orders/health` - Service health check

## ⚙️ Configuration

### Environment Variables
```yaml
# Database Configuration
DB_CONNECTION_IP: localhost
DB_CONNECTION_PORT: 3306
DB_CONNECTION_USERNAME: user
DB_CONNECTION_PASSWORD: password

# Service URLs
GATEWAY_HOST: http://nginx
```

### Nginx Configuration
```nginx
upstream user_service {
    server user-service-1:4020;
    server user-service-2:4020;
}
# Similar configuration for product and order services
```

## 📊 Monitoring & Health

### Health Check Endpoints
- User Service: http://localhost/health/user
- Product Service: http://localhost/health/product
- Order Service: http://localhost/health/order

### Scaling Services
```bash
docker-compose up --scale user-service=2 --scale product-service=2 --scale order-service=2
```

## 🔧 Troubleshooting

### Common Issues

1. **Service Connection Failures**
   - Check service health endpoints
   - Verify nginx configuration
   - Ensure services are on the same Docker network

2. **Database Connection Issues**
   - Verify database credentials
   - Check database container status
   - Ensure database initialization completed

3. **Load Balancing Problems**
   - Check nginx logs: `docker-compose logs nginx`
   - Verify service discovery
   - Check service registration

### Debugging Tips
- Use `docker-compose logs <service-name>` for service logs
- Check application logs in `/var/log/`
- Verify network connectivity between services

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.
