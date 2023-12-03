package com.kou5321.jobPortalWebsite.job.service;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    public JobPosting getJobPostingById(String id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job Posting not found with ID: " + id));
    }

    public long countDistinctCompanies() {
        return jobPostingRepository.countDistinctCompany();
    }
}
