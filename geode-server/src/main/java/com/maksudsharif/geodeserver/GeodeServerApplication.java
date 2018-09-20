package com.maksudsharif.geodeserver;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableLogging;
import org.springframework.data.gemfire.config.annotation.EnablePdx;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableDiscoveryClient
@CacheServerApplication
@Log4j2
@EnableLogging
@EnablePdx(readSerialized = true)
@EnableClusterConfiguration
public class GeodeServerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GeodeServerApplication.class, args);
    }

    @Bean(value = "gemfireProperties")
    Properties gemfireProperties(DiscoveryClient discoveryClient,
                                 @Value("${spring.data.gemfire.cache.name}") String name) {

        Properties gemfireProperties = new Properties();
        log.info("Retrieving locator instances...");
        List<ServiceInstance> locatorInstances = discoveryClient.getInstances("geode-locator");
        String locators = locatorInstances.parallelStream()
                .map(ServiceInstance::getMetadata)
                .map(metadata -> metadata.get("geodeLocatorId"))
                .distinct()
                .collect(Collectors.joining(","));
        log.info(String.format("Found locators: [%s]", locators));
        if(locators.isEmpty()){
            log.error("Found no other locators, must have locators");
            throw new IllegalStateException();
        } else {
            gemfireProperties.put("locators", locators);
        }

        gemfireProperties.put("name", name);

        return gemfireProperties;
    }
}
