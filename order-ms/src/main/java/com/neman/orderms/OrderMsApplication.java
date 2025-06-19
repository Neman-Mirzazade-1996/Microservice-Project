package com.neman.orderms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages="com.neman.orderms.Client")
public class OrderMsApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMsApplication.class, args);
    }

}
