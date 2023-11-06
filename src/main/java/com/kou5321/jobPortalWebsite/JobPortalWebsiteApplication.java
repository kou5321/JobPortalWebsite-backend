package com.kou5321.jobPortalWebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class JobPortalWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalWebsiteApplication.class, args);
	}

}
