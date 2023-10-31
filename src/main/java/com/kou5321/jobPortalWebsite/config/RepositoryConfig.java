package com.kou5321.jobPortalWebsite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

public class RepositoryConfig {
    @Configuration
    @EnableJpaAuditing
    @EnableJpaRepositories(basePackages = "com.kou5321.jobPortalWebsite.user.repository")
    public class JpaConfig {}

    @Configuration
    @EnableMongoRepositories(basePackages = "com.kou5321.jobPortalWebsite.job.repository")
    public class MongoConfig {}
}
