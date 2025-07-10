package com.neman.springcloud.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GatewayConfigurationTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void testRouteConfiguration() {
        Flux<Route> routes = routeLocator.getRoutes();
        List<Route> routeList = routes.collectList().block();
        
        assertNotNull(routeList);
        assertTrue(routeList.size() >= 4, "Should have at least 4 routes configured");
        
        // Check that all expected routes exist
        List<String> routeIds = routeList.stream()
                .map(Route::getId)
                .toList();
        
        assertTrue(routeIds.contains("user-service"), "Should contain user-service route");
        assertTrue(routeIds.contains("product-service"), "Should contain product-service route");
        assertTrue(routeIds.contains("order-service"), "Should contain order-service route");
        assertTrue(routeIds.contains("openapi"), "Should contain openapi route");
    }

    @Test
    void testUserServiceRoute() {
        Flux<Route> routes = routeLocator.getRoutes();
        List<Route> routeList = routes.collectList().block();
        
        assertNotNull(routeList);
        
        Route userServiceRoute = routeList.stream()
                .filter(route -> "user-service".equals(route.getId()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(userServiceRoute, "User service route should exist");
        assertTrue(userServiceRoute.getUri().toString().contains("nginx"), 
                "User service route should use nginx");
        assertTrue(userServiceRoute.getUri().toString().contains("80"), 
                "User service route should use port 80");
    }

    @Test
    void testProductServiceRoute() {
        Flux<Route> routes = routeLocator.getRoutes();
        List<Route> routeList = routes.collectList().block();
        
        assertNotNull(routeList);
        
        Route productServiceRoute = routeList.stream()
                .filter(route -> "product-service".equals(route.getId()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(productServiceRoute, "Product service route should exist");
        assertTrue(productServiceRoute.getUri().toString().contains("nginx"), 
                "Product service route should use nginx");
        assertTrue(productServiceRoute.getUri().toString().contains("80"), 
                "Product service route should use port 80");
    }

    @Test
    void testOrderServiceRoute() {
        Flux<Route> routes = routeLocator.getRoutes();
        List<Route> routeList = routes.collectList().block();
        
        assertNotNull(routeList);
        
        Route orderServiceRoute = routeList.stream()
                .filter(route -> "order-service".equals(route.getId()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(orderServiceRoute, "Order service route should exist");
        assertTrue(orderServiceRoute.getUri().toString().contains("nginx"), 
                "Order service route should use nginx");
        assertTrue(orderServiceRoute.getUri().toString().contains("80"), 
                "Order service route should use port 80");
    }
}