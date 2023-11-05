package com.kou5321.jobPortalWebsite.job.controller;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.*;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class JobPostingController {
    @Autowired
    JobPostingRepository jobPostingRepository;
    @Autowired
    JobSearchRepositoryImpl jobSearchRepository;

//    @RequestMapping("/")
//    @Hidden
//    public void redirect(HttpServletResponse response) throws IOException {
//        response.sendRedirect("/swagger-ui.html");
//    }

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
}
