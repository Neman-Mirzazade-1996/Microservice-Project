#!/bin/bash

# Keycloak Integration Test Script

BASE_URL="http://localhost"
KEYCLOAK_URL="http://localhost:8080"

echo "=== Keycloak Integration Test ==="

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 30

# Test 1: Health check
echo -e "\n1. Testing health endpoint..."
curl -s "$BASE_URL/health" || echo "Health check failed"

# Test 2: Keycloak admin access
echo -e "\n2. Testing Keycloak access..."
curl -s "$KEYCLOAK_URL/realms/master" > /dev/null && echo "Keycloak is accessible" || echo "Keycloak access failed"

# Test 3: User registration
echo -e "\n3. Testing user registration..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/users/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "test123",
    "phone": "1234567890",
    "address": "123 Test St",
    "city": "Test City",
    "country": "Test Country",
    "postalCode": "12345"
  }')

echo "Registration response: $REGISTER_RESPONSE"

# Test 4: User login with admin credentials
echo -e "\n4. Testing admin login..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/users/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com", 
    "password": "admin123"
  }')

echo "Login response: $LOGIN_RESPONSE"

# Extract token (simple extraction, assumes JSON format)
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -n "$TOKEN" ]; then
  echo "Token received: ${TOKEN:0:50}..."
  
  # Test 5: Access secured endpoint
  echo -e "\n5. Testing secured endpoint with token..."
  SECURED_RESPONSE=$(curl -s -X GET "$BASE_URL/api/v1/users/1" \
    -H "Authorization: Bearer $TOKEN")
  
  echo "Secured endpoint response: $SECURED_RESPONSE"
else
  echo "No token received - authentication failed"
fi

echo -e "\n=== Test completed ==="