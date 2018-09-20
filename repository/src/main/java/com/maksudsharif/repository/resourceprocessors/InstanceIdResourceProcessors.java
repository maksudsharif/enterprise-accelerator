package com.maksudsharif.repository.resourceprocessors;

import com.maksudsharif.repository.model.InstanceIdAware;
import com.maksudsharif.repository.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component
public class InstanceIdResourceProcessors
{
    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceIdResourceProcessors.class);

    @Value("#{new org.springframework.hateoas.Link('${eureka.instance.instance-id}', 'instance-ref')}")
    private Link instanceLink;

    @Bean
    public ResourceProcessor<PagedResources<Resource<InstanceIdAware>>> pagedResourcesProcessor() {
        return new ResourceProcessor<PagedResources<Resource<InstanceIdAware>>>()
        {
            @Override
            public PagedResources<Resource<InstanceIdAware>> process(PagedResources<Resource<InstanceIdAware>> resource)
            {
                if(!resource.hasLink("instance-ref")) {
                    resource.add(instanceLink);
                }
                return resource;
            }
        };
    }
}
