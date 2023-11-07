package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
}
