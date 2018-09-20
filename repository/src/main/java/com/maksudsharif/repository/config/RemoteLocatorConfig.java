package com.maksudsharif.repository.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Log4j2
@Profile({"geode_remote"})
public class RemoteLocatorConfig
{

    @Value("${caching.gemfire.locator:''}")
    String locators;

    @Bean
    public Iterable<ConnectionEndpoint> locatorDefinitionsRemote() {
        log.info("Using remote geode locators: [{}]", locators);
        return Arrays.stream(locators.split(",")).map(ConnectionEndpoint::parse).collect(Collectors.toList());
    }
}
