package com.example.RedisForOrderManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching//it activate spring cache system without this we cannot able to use the annotation
//related to it
public class RedisForOrderManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisForOrderManagementApplication.class, args);
    }
}
