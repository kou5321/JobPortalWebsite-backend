package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
public class JobSearchRepositoryImpl implements JobSearchRepository {
    @Autowired
    MongoClient client;
    @Autowired
    MongoConverter converter;

    @Override
    public List<JobPosting> findByText(String text) {
        final List<JobPosting> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("jobListing");
        MongoCollection<Document> collection = database.getCollection("JobCrawler");

        // Use aggregation to perform a text search on the collection
        // 1, searching in the "techs", "desc", and "profile" fields
        // 2. It limits the output to the first 5 documents
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(
                        new Document("$search",
                                new Document("text",
                                        new Document("query", text)
                                                .append("path", Arrays.asList("company", "jobTitle"))))
                ));

        result.forEach(doc -> posts.add(converter.read(JobPosting.class, doc)));

        log.info("Search for text '{}' returned {} results.", text, posts.size());
        return posts;
    }
}
