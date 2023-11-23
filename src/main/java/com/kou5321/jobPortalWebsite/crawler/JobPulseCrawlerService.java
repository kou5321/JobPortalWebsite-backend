package com.kou5321.jobPortalWebsite.crawler;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@EnableScheduling
public class JobPulseCrawlerService {
    private final RestTemplate restTemplate;
    private final JobPostingRepository jobPostingRepository;

    @Autowired
    public JobPulseCrawlerService(JobPostingRepository jobPostingRepository) {
        this.restTemplate = new RestTemplate();
        this.jobPostingRepository = jobPostingRepository;
    }


    // executed every 6 hours
//    @Scheduled(cron = "0 0 */6 * * ?")
    public void fetchAndSaveJobPostings() {
        int totalJobPostingsToFetch = 100;
        int pageSize = 20;
        int totalPages = totalJobPostingsToFetch / pageSize;

        try {
            log.info("Deleting all existing job postings");
//            jobPostingRepository.deleteAll();

            log.info("begin web crawler");
            for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
                String apiUrl = "https://job-pulse.uc.r.appspot.com/jobs/sde?yoe_less_than=5&page_number=" + pageNumber + "&page_size=" + pageSize;
                ResponseEntity<List<JobPosting>> responseEntity =
                        restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<JobPosting>>() {});

                List<JobPosting> jobPostings = responseEntity.getBody();
                log.info("Job postings fetched from page {}: {}", pageNumber, jobPostings);

                if (jobPostings != null) {
                    jobPostingRepository.saveAll(jobPostings);
                }

                // Optional: Sleep between requests to avoid hitting the server too hard
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching or saving job postings: ", e);
            // You could also rethrow the exception if you have a global exception handler
            // throw new ServiceException("Error fetching job postings", e);
        }
    }
}
