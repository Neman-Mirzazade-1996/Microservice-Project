# Microservice Project

This repository contains a small Spring Boot based microservice system with three services:

- **User Service** - manages user accounts.
- **Product Service** - manages products and stock levels.
- **Order Service** - handles customer orders and communicates with the other services through [Feign](https://spring.io/projects/spring-cloud-openfeign) clients.

Services are containerised and orchestrated locally with **Docker Compose**. An **Nginx** container acts as a simple load balancer and reverse proxy for all services.

## Project Structure

```
├── User-ms     # User microservice
├── Product-ms  # Product microservice
├── Order-ms    # Order microservice
├── nginx       # Nginx config used by docker-compose
├── docker-compose.yml
└── build.gradle (root) with helper tasks for building Docker images
```

Each microservice is a Gradle project. The build script generates a Dockerfile and jar in `build/docker/`. Images can be pushed to Docker Hub using credentials from `gradle.properties`.

## Running Locally

Ensure Docker and Docker Compose are installed. To start all databases, services and nginx run:

```bash
docker-compose up --build
```

The services will be reachable via nginx on the following ports:

- `http://localhost:4020` – **user-service**
- `http://localhost:4010` – **product-service**
- `http://localhost:4030` – **order-service**

### Scaling Services

Docker Compose can run multiple instances of each service. Example:

```bash
docker-compose up --scale user-service=2 --scale product-service=2 --scale order-service=2
```

Nginx automatically distributes requests between the available instances.

### Configuration

Each service has its own MySQL database container. Connection details are configured via environment variables in `docker-compose.yml` and exposed in `src/main/resources/application.yml` of each service.

The order service resolves other services via `USER_SERVICE_URL` and `PRODUCT_SERVICE_URL`. By default these point at the nginx container.

## API Overview

### User Service (`/api/v1/users`)
- `POST /createUser` – create a user
- `GET /getUser/{id}` – get user details by id

### Product Service (`/api/v1/products`)
- `POST /create` – create product
- `PUT /update/{id}` – update product
- `DELETE /delete/{id}` – delete product
- `GET /get/{id}` – fetch single product
- `GET /getAll` – list all products
- `POST /increaseStock/{productId}/{qty}` – increase stock quantity
- `POST /decreaseStock/{productId}/{qty}` – decrease stock quantity

### Order Service (`/api/v1/orders`)
- `POST /create` – create order
- `GET /findAll` – list orders
- `GET /getOrderById/{id}` – fetch order by id
- `GET /product/{productId}` – retrieve product info via product service
- `GET /user/{userId}` – retrieve user info via user service

Swagger UI is enabled in each service and reachable at `/swagger-ui/index.html` on the respective ports when running locally.

Health checks are available via Spring Boot Actuator at `/actuator/health`.

## Building Docker Images

Each microservice can build and push its Docker image individually using Gradle tasks defined in the service's `build.gradle`. The root project also provides a helper task to build and push all images:

```bash
./gradlew buildAllDockerImages
```

## License

This project is provided as-is for demonstration purposes.
>>>>>>> 699801f (docs: add comprehensive README)
