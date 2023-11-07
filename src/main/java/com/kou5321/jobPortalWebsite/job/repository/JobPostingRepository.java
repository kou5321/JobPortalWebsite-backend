package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import jakarta.persistence.SecondaryTable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
}
