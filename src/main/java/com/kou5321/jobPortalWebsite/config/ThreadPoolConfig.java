package com.kou5321.jobPortalWebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        int corePoolSize = 10;
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(corePoolSize);

        // Set the keep-alive time, thread factory, and other configurations if needed
        scheduledThreadPoolExecutor.setKeepAliveTime(0L, TimeUnit.MILLISECONDS);
        scheduledThreadPoolExecutor.setThreadFactory(Executors.defaultThreadFactory());

        // Add a shutdown hook to properly shut down the executor
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduledThreadPoolExecutor.shutdown();
            try {
                if (!scheduledThreadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduledThreadPoolExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduledThreadPoolExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        return scheduledThreadPoolExecutor;
    }
}