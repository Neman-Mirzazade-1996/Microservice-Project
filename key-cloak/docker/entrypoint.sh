#!/bin/sh
set -e
echo "Waiting for Keycloak..."
sleep 30
echo "Starting Keycloak setup"
exec java -jar /app/key-cloak.jar
