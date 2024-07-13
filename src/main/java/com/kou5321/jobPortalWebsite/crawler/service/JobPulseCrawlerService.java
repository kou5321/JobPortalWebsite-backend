package com.kou5321.jobPortalWebsite.crawler.service;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class JobPulseCrawlerService {
    private final RestTemplate restTemplate;
    private final JobPostingRepository jobPostingRepository;
    private final ScheduledExecutorService scheduledExecutorService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public JobPulseCrawlerService(JobPostingRepository jobPostingRepository, ScheduledExecutorService scheduledExecutorService, RabbitTemplate rabbitTemplate) {
        this.restTemplate = new RestTemplate();
        this.jobPostingRepository = jobPostingRepository;
        this.scheduledExecutorService = scheduledExecutorService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void scheduleTasks() {
        scheduledExecutorService.scheduleAtFixedRate(this::scheduleJobPulseCrawl, 0, 12, TimeUnit.HOURS);
    }

//    @Scheduled(cron = "0 0 */12 * * ?") // Execute every 12 hours
    public void scheduleJobPulseCrawl() {
        fetchAndSaveJobPostings();
    }

    public void fetchAndSaveJobPostings() {
        int totalJobPostingsToFetch = 20;
        int pageSize = 20;
        int totalPages = totalJobPostingsToFetch / pageSize;

        try {
            log.info("Deleting all existing job postings");
            jobPostingRepository.deleteAll();

            log.info("begin web crawler");
            for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
                String apiUrl = "https://job-pulse.uc.r.appspot.com/jobs/sde?yoe_less_than=5&page_number=" + pageNumber + "&page_size=" + pageSize;
                ResponseEntity<List<JobPosting>> responseEntity =
                        restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<JobPosting>>() {});

                List<JobPosting> jobPostings = responseEntity.getBody();
                log.info("Job postings fetched from page {}: {}", pageNumber, jobPostings);

                if (jobPostings != null) {
                    rabbitTemplate.convertAndSend("jobQueue", jobPostings);
//                    jobPostingRepository.saveAll(jobPostings);
                }

                // Sleep between requests to avoid hitting the server too hard
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching or saving job postings: ", e);
        }
    }
}
