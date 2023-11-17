package com.kou5321.jobPortalWebsite.job.controller;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import com.kou5321.jobPortalWebsite.job.repository.JobSearchRepositoryImpl;
import com.kou5321.jobPortalWebsite.job.service.JobPostingService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
@Slf4j
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
        response.sendRedirect("/" +
                "swagger-ui.html");
    }

    @GetMapping("/getAllJobPosts")
    public List<JobPosting> getAllPosts() {
        return jobPostingRepository.findAll();
    }

    @PostMapping("/addJobPost")
    @PreAuthorize("hasRole('ADMIN')")
    public JobPosting addPost(@RequestBody JobPosting post) {
        log.info("add a job pos: " + post);
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
