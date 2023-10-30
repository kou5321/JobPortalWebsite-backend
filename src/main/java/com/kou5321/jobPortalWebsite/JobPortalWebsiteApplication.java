package com.kou5321.jobPortalWebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class JobPortalWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalWebsiteApplication.class, args);
	}

}
