package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JobSearchRepositoryImpl implements JobSearchRepository {
    private final MongoClient client;
    private final MongoConverter converter;

    public Page<JobPosting> findByTextAndCountry(String text, String country, Integer maxYearsOfExperience, Pageable pageable) {
        List<JobPosting> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("jobListing");
        MongoCollection<Document> collection = database.getCollection("JobCrawler");

        Bson filter = new Document();

        // Add text search condition if text is provided and not empty
        if (text != null && !text.isEmpty()) {
            filter = Filters.and(filter, new Document("$text", new Document("$search", text)));
        }

        if (maxYearsOfExperience != null) {
            filter = Filters.and(filter, Filters.lte("yoe", maxYearsOfExperience));
        }

        // Add country filter condition if country is provided and not empty
        if (country != null && !country.isEmpty()) {
            filter = Filters.and(filter, Filters.eq("location", country));
        }

        long total = collection.countDocuments(filter);

        // cannot sort data because crawler
        Bson sortOperation = Sorts.descending("apply_link");

        List<Bson> aggregationPipeline = Arrays.asList(
                new Document("$match", filter),
                new Document("$sort", sortOperation),
                new Document("$skip", pageable.getOffset()),
                new Document("$limit", pageable.getPageSize())
        );

        AggregateIterable<Document> result = collection.aggregate(aggregationPipeline);

        result.forEach(doc -> posts.add(converter.read(JobPosting.class, doc)));

        return new PageImpl<>(posts, pageable, total);
    }


}
