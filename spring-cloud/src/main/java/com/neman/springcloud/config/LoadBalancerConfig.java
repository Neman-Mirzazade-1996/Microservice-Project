package com.neman.springcloud.config;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
public class LoadBalancerConfig {

    @Bean
    public ServiceInstanceListSupplier userServiceInstanceSupplier() {
        return new StaticServiceInstanceListSupplier("user-service", 
            List.of(
                new DefaultServiceInstance("user-service-1", "user-service", "user-service-1", 4020, false),
                new DefaultServiceInstance("user-service-2", "user-service", "user-service-2", 4020, false)
            )
        );
    }

    @Bean  
    public ServiceInstanceListSupplier productServiceInstanceSupplier() {
        return new StaticServiceInstanceListSupplier("product-service",
            List.of(
                new DefaultServiceInstance("product-service-1", "product-service", "product-service-1", 4010, false),
                new DefaultServiceInstance("product-service-2", "product-service", "product-service-2", 4010, false)
            )
        );
    }

    @Bean
    public ServiceInstanceListSupplier orderServiceInstanceSupplier() {
        return new StaticServiceInstanceListSupplier("order-service",
            List.of(
                new DefaultServiceInstance("order-service-1", "order-service", "order-service-1", 4030, false),
                new DefaultServiceInstance("order-service-2", "order-service", "order-service-2", 4030, false)
            )
        );
    }

    private static class StaticServiceInstanceListSupplier implements ServiceInstanceListSupplier {
        private final String serviceId;
        private final List<ServiceInstance> instances;

        public StaticServiceInstanceListSupplier(String serviceId, List<ServiceInstance> instances) {
            this.serviceId = serviceId;
            this.instances = instances;
        }

        @Override
        public String getServiceId() {
            return serviceId;
        }

        @Override
        public Flux<List<ServiceInstance>> get() {
            return Flux.just(instances);
        }
    }
}