#!/bin/bash

# Build and Test Script for Microservice Project
# This script builds all necessary JAR files and tests the Keycloak setup

echo "=== Building Microservice Project JAR Files ==="

# Function to build a service
build_service() {
    local service_name=$1
    echo "Building $service_name..."
    cd $service_name
    chmod +x gradlew
    ./gradlew build -x test
    
    # Copy JAR to docker directory
    jar_file=$(find build/libs -name "*.jar" | grep -v plain | head -1)
    if [ -f "$jar_file" ]; then
        cp "$jar_file" docker/$(basename $service_name).jar
        echo "✓ $service_name JAR built and copied to docker directory"
    else
        echo "✗ Failed to build $service_name JAR"
        exit 1
    fi
    cd ..
}

# Build all services
echo "Building key-cloak service..."
build_service "key-cloak"

echo "Building user-ms service..."
build_service "user-ms"

echo "Building product-ms service..."
build_service "product-ms"

echo "Building order-ms service..."
build_service "order-ms"

echo "Building spring-cloud service..."
build_service "spring-cloud"

echo ""
echo "=== All JAR files built successfully! ==="
echo ""
echo "=== Testing Keycloak Setup ==="

# Start the essential services for testing
echo "Starting PostgreSQL and Keycloak..."
docker compose up -d postgres my-keycloak

echo "Waiting for services to be healthy..."
sleep 60

echo "Starting Keycloak setup..."
docker compose up keycloak-setup

echo ""
echo "=== Testing complete! ==="
echo ""
echo "Next steps:"
echo "1. Check if keycloak-setup completed successfully"
echo "2. Test user services: docker compose up user-db user-service-1 user-service-2"
echo "3. Check service health: docker compose ps"
echo ""
echo "To access Keycloak admin console:"
echo "URL: http://localhost:8080"
echo "Username: admin"
echo "Password: admin"
echo ""
echo "Expected realm: microservice-realm"
echo "Expected client: microservice-client"