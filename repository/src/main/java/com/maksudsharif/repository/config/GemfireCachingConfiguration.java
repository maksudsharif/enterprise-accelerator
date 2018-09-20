package com.maksudsharif.repository.config;

import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties;
import org.springframework.data.gemfire.config.annotation.EnableLogging;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.mapping.MappingPdxSerializer;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

@Profile("dev")
@Configuration
@EnableGemfireCaching
@EnableGemFireProperties
@EnableEntityDefinedRegions(basePackages = "com.maksudsharif.repository.model", clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableCachingDefinedRegions
@ClientCacheApplication
@EnablePdx(serializerBeanName = "mappingPdxSerializer")
@EnableLogging
public class GemfireCachingConfiguration
{

    @Bean
    public ClientCacheConfigurer clientCacheConfigurer(Iterable<ConnectionEndpoint> locatorDefinitions) {
        return (beanName, clientCacheFactoryBean) -> {
            clientCacheFactoryBean.addLocators(locatorDefinitions);
        };
    }

    @Bean(name = "mappingPdxSerializer")
    public MappingPdxSerializer mappingPdxSerializer() {
        return new MappingPdxSerializer();
    }
}
