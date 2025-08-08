# Docker Loglarını Saxlama Skripti
$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
$logDir = "logs_$timestamp"

Write-Host "Creating log directory: $logDir"
New-Item -ItemType Directory -Path $logDir -Force

# Bütün servislərin logları
Write-Host "Saving all services logs..."
docker-compose logs --no-color > "$logDir\all-services.txt"

# Hər servisin ayrıca logları
$services = @("my-keycloak", "keycloak-setup", "postgres", "user-db", "order-db", "product-db",
              "user-service-1", "user-service-2", "order-service-1", "order-service-2",
              "product-service-1", "product-service-2", "spring-cloud-gateway", "nginx")

foreach ($service in $services) {
    Write-Host "Saving logs for: $service"
    docker-compose logs $service --no-color > "$logDir\$service.txt" 2>$null
}

# Servis statusları
Write-Host "Saving service status..."
docker-compose ps > "$logDir\service-status.txt"

# Docker sistem məlumatları
Write-Host "Saving docker info..."
docker info > "$logDir\docker-info.txt" 2>$null

Write-Host "Logs saved to directory: $logDir"
Write-Host "Main files:"
Write-Host "  - all-services.txt (bütün servislər)"
Write-Host "  - my-keycloak.txt (Keycloak server)"
Write-Host "  - keycloak-setup.txt (Setup servisi)"
Write-Host "  - service-status.txt (Servis statusları)"
