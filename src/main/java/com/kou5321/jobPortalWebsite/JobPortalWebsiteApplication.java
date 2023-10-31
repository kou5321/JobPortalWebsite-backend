package com.kou5321.jobPortalWebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableMongoRepositories(basePackages = "com.kou5321.jobPortalWebsite.job.repository")
//@EnableJpaRepositories(basePackages = "com.kou5321.jobPortalWebsite.user.repository")
public class JobPortalWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalWebsiteApplication.class, args);
	}

}
