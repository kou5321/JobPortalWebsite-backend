package com.kou5321.jobPortalWebsite;

import com.kou5321.jobPortalWebsite.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
