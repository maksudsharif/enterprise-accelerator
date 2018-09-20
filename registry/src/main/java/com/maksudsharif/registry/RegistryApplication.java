package com.maksudsharif.registry;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@EnableAdminServer
@EnableDiscoveryClient
public class RegistryApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(RegistryApplication.class, args);
    }
}
