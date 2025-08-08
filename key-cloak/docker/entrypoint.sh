#!/bin/sh
set -e
echo "Waiting for Keycloak..."
until curl -fsS http://my-keycloak:8080/health/ready; do
  echo "Keycloak not ready yet — sleeping 5s"
  sleep 5
done
echo "Keycloak ready — starting setup"
exec java -jar /app/key-cloak.jar
