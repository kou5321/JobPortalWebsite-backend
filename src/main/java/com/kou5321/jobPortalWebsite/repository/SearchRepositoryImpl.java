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

        AggregateIterable<Document> result = collection
                .aggregate(Arrays.asList(new Document("$search",
                                new Document("text",
                                        new Document("query", text)
                                                .append("path", Arrays.asList("techs", "desc", "profile")))),
//                        new Document("$sort",
//                                new Document("exp", 1L)),
                        new Document("$limit", 5L)));

        result.forEach(doc -> posts.add(converter.read(Post.class, doc)));
        // test if it is being executed
        log.info("Test successful!");
        Post test = new Post("testName", "testXXX", 1, new String[] {"hhh"});
        posts.add(test);
        return posts;
    }
}
