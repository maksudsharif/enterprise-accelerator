package com.maksudsharif.repository.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Log4j2
@Profile({"geode_local"})
public class LocalLocatorConfig
{

    @Bean
    public Iterable<ConnectionEndpoint> locatorDefinitionsLocal(DiscoveryClient discoveryClient) {
        log.info("Using local geode locators");
        return discoveryClient.getInstances("geode-locators").stream()
                .map(ServiceInstance::getMetadata)
                .map(metadata -> metadata.get("geodeLocatorId"))
                .filter(Objects::nonNull)
                .map(ConnectionEndpoint::parse)
                .collect(Collectors.toList());
    }

}
