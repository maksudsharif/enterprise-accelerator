package com.maksudsharif.geodelocator;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableHttpService;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;
import org.springframework.data.gemfire.config.annotation.EnablePdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@EnableLocator
@EnableManager
@EnablePdx(readSerialized = true)
@EnableClusterConfiguration
@Log4j2
public class LocatorConfiguration
{
    @Bean(value = "gemfireProperties")
    Properties gemfireProperties(DiscoveryClient discoveryClient,
                                 @Value("${spring.data.gemfire.locator.instance}") String currentLocatorInstanceId,
                                 @Value("${spring.data.gemfire.locator.enableClusterConfiguration}") boolean enableClusterConfiguration,
                                 @Value("${spring.data.gemfire.locator.name}") String name,
                                 @Value("${spring.data.gemfire.locator.vip}") String vipLocator) {
        Properties gemfireProperties = new Properties();
        log.info("Retrieving locator instances...");
        List<ServiceInstance> locatorInstances = discoveryClient.getInstances("geode-locator");
        List<String> locators = new ArrayList<>();
        if(StringUtils.isNotEmpty(vipLocator)) {
            locators.add(vipLocator);
        }
        List<String> discoveredLocators = locatorInstances.parallelStream()
                .map(ServiceInstance::getMetadata)
                .map(metadata -> metadata.get("geodeLocatorId"))
                .filter(locatorId -> !currentLocatorInstanceId.equalsIgnoreCase(locatorId))
                .distinct()
                .collect(Collectors.toList());

        locators.addAll(discoveredLocators);
        String finalLocators = locators.stream().collect(Collectors.joining(","));
        log.info(String.format("Found locators: [%s]", finalLocators));

        if(discoveredLocators.isEmpty() && StringUtils.isEmpty(vipLocator)){
            log.info("Found no other locators, I am first");
        } else {
            gemfireProperties.put("locators", finalLocators);
        }

        gemfireProperties.put("name", name);
        gemfireProperties.put("enable-cluster-configuration", enableClusterConfiguration);

        return gemfireProperties;
    }
}
