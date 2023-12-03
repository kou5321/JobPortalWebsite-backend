package com.kou5321.jobPortalWebsite.job.config;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {
        private final MongoTemplate mongoTemplate;

        @PostConstruct
        public void initIndices() {
            TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                    .onField("company")
                    .onField("title")
                    .onField("location")
                    .build();

            mongoTemplate.indexOps(JobPosting.class).ensureIndex(textIndex);
        }
}
