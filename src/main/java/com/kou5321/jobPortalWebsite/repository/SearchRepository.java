package com.kou5321.jobPortalWebsite.repository;

import com.kou5321.jobPortalWebsite.model.Post;

import java.util.List;

public interface SearchRepository {
    List<Post> findByText(String text);
}
