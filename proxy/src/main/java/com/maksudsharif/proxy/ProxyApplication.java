package com.maksudsharif.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ProxyApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ProxyApplication.class, args);
    }

    @Bean
    RouteLocator resourceRouteLocator(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route(r -> r.path("/admin/**").uri("lb://boot-admin/admin"))
                .route(r -> r.path("/eureka/**").uri("lb://registry"))
                .build();
    }
}
