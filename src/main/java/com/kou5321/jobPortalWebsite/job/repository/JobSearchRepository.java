package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobSearchRepository {
    Page<JobPosting> findByText(String text, Pageable pageable);
}
