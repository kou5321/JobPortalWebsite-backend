package com.kou5321.jobPortalWebsite.job.service;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class JobPostingService {
    @Autowired
    private JobPostingRepository jobPostingRepository;

    public JobPosting getJobPostingById(String id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job Posting not found with ID: " + id));
    }

    public Set<JobPosting> getJobPostingsFromIds(Set<String> appliedJobPostingIds) {
        Iterable<JobPosting> jobPostings = jobPostingRepository.findAllById(appliedJobPostingIds);
        Set<JobPosting> jobPostingSet = new HashSet<>();
        jobPostings.forEach(jobPostingSet::add);
        return jobPostingSet;
    }

    public long countDistinctCompanies() {
        // Assuming the method countDistinctCompany is already defined in JobPostingRepository
        return jobPostingRepository.countDistinctCompany();
    }
}
