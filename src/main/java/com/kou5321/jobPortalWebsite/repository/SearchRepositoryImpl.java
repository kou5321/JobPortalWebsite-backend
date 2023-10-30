package com.kou5321.jobPortalWebsite.repository;

import com.kou5321.jobPortalWebsite.model.Post;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SearchRepositoryImpl implements SearchRepository {

    @Autowired
    MongoClient client;
    @Autowired
    MongoConverter converter;

    @Override
    public List<Post> findByText(String text) {
        final List<Post> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("jobListing");
        MongoCollection<Document> collection = database.getCollection("JobPost");

        // Use aggregation to perform a text search on the collection
        // 1, searching in the "techs", "desc", and "profile" fields
        // 2. It limits the output to the first 5 documents
        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(
                        new Document("$search",
                                new Document("text",
                                        new Document("query", text)
                                                .append("path", Arrays.asList("techs", "desc", "profile")))),
                        new Document("$limit", 5L)
                ));

        result.forEach(doc -> posts.add(converter.read(Post.class, doc)));

        log.info("Search for text '{}' returned {} results.", text, posts.size());
        return posts;
    }
}
