package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.Post;

import java.util.List;

public interface SearchRepository {
    List<Post> findByText(String text);
}
