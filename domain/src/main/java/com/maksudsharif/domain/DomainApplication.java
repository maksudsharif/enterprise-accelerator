package com.maksudsharif.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.maksudsharif.domain")
public class DomainApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(DomainApplication.class, args);
    }
}
