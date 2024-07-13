package com.kou5321.jobPortalWebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ThreadPoolConfig {

//    @Bean
//    public ExecutorService taskExecutor() {
//        return Executors.newFixedThreadPool(10); // Adjust the number of threads as needed
//    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(10); // Adjust the number of threads as needed
    }
}