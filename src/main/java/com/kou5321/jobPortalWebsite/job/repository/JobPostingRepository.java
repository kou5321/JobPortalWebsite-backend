package com.kou5321.jobPortalWebsite.job.repository;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
    Page<JobPosting> findAll(Pageable pageable);

    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$company' } }",
            "{ '$count': 'uniqueCompanies' }"
    })
    long countDistinctCompany();

    @Query(value = "{'date_added': {$regex: ?0, $options: 'i'}}")
    List<JobPosting> findJobPostingsByDateAdded(String dateRegex);
}
