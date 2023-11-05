package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;

import java.util.List;

public interface JobSearchRepository {
    List<JobPosting> findByText(String text);
}
