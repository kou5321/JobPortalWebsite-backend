package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.bson.conversions.Bson;
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
    public Page<JobPosting> findByText(String text, Pageable pageable) {
        List<JobPosting> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("jobListing");
        MongoCollection<Document> collection = database.getCollection("JobCrawler");

        Bson textSearchQuery = new Document("$text",
                new Document("$search", text));

        long total = collection.countDocuments(textSearchQuery);

        List<Bson> aggregationPipeline = Arrays.asList(
                new Document("$match", textSearchQuery),
                new Document("$skip", pageable.getOffset()),
                new Document("$limit", pageable.getPageSize())
        );

        AggregateIterable<Document> result = collection.aggregate(aggregationPipeline);

        result.forEach(doc -> posts.add(converter.read(JobPosting.class, doc)));

        return new PageImpl<>(posts, pageable, total);
    }
}
