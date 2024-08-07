package com.kou5321.jobPortalWebsite.job.controller;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import com.kou5321.jobPortalWebsite.job.repository.JobSearchRepositoryImpl;
import com.kou5321.jobPortalWebsite.job.service.JobPostingService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class JobPostingController {
    private final JobPostingRepository jobPostingRepository;
    private final JobSearchRepositoryImpl jobSearchRepository;
    private final JobPostingService jobPostingService;

//    @RequestMapping("/")
//    @Hidden
    // TODO: @Hidden annotation should be removed when deployment
//    public void redirect(HttpServletResponse response) throws IOException {
//        response.sendRedirect("/" +
//                "swagger-ui.html");
//    }

    @GetMapping("/getAllJobPosts")
    public Page<JobPosting> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jobPostingRepository.findAll(pageable);
    }

    @PostMapping("/addJobPost")
    @PreAuthorize("hasRole('ADMIN')")
    public JobPosting addPost(@RequestBody JobPosting post) {
        log.info("add a job pos: " + post);
        return jobPostingRepository.save(post);
    }

    @GetMapping("/jobPost/search")
    public Page<JobPosting> searchPost(
            @RequestParam(required = false, defaultValue = "") String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer maxYearsOfExperience) {
        Pageable pageable = PageRequest.of(page, size);
        return jobSearchRepository.findByTextAndCountry(text, country, maxYearsOfExperience, pageable);
    }

    @GetMapping("/findJobById/{id}")
    public JobPosting getJobById(@PathVariable String id) {
        return jobPostingService.getJobPostingById(id);
    }

    @PostMapping("/getCompanyNumber")
    public long getCompanyNum() {
        return jobPostingService.countDistinctCompanies();
    }

    @GetMapping("/getJobPostsToday")
    public List<JobPosting> getJobPostsToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy"); // Format to match the date part
        String today = sdf.format(new Date());
        String dateRegex = "^" + today; // Regex to match the beginning of the string
        return jobPostingRepository.findJobPostingsByDateAdded(dateRegex);
    }


    @GetMapping("/getJobPostsBySubscription")
    public List<JobPosting> getJobPostsBySubscription(
            @RequestParam String location,
            @RequestParam int preferredYear) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy"); // Format to match the date part
        String today = sdf.format(new Date());
        String dateRegex = "^" + today; // Regex to match the beginning of the string
        return jobPostingRepository.findByDateLocationAndYoe(dateRegex, location, preferredYear);
    }
}
