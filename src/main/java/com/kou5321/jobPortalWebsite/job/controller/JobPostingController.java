package com.kou5321.jobPortalWebsite.job.controller;

import com.kou5321.jobPortalWebsite.crawler.GithubCrawlerService;
import com.kou5321.jobPortalWebsite.crawler.JobPulseCrawlerService;
import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.*;
import com.kou5321.jobPortalWebsite.job.service.JobPostingService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
public class JobPostingController {
    @Autowired
    JobPostingRepository jobPostingRepository;
    @Autowired
    JobSearchRepositoryImpl jobSearchRepository;
    @Autowired
    JobPostingService jobPostingService;

    @RequestMapping("/")
    @Hidden
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    @GetMapping("/getAllJobPosts")
    public List<JobPosting> getAllPosts() {
        return jobPostingRepository.findAll();
    }

    @PostMapping("/addJobPost")
    public JobPosting addPost(@RequestBody JobPosting post) {
        return jobPostingRepository.save(post);
    }

    @GetMapping("/jobPost/search/text={text}")
    public List<JobPosting> searchPost(@PathVariable String text) {
        return jobSearchRepository.findByText(text);
    }

    @GetMapping("/findJobById/{id}")
    public JobPosting getJobById(@PathVariable String id) {
        return jobPostingService.getJobPostingById(id);
    }
}
