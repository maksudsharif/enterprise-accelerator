package com.maksudsharif.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.gemfire.config.annotation.EnableLogging;

@SpringBootApplication
@EnableDiscoveryClient
@EnableLogging
public class RepositoryApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RepositoryApplication.class, args);
    }

}
