package com.kou5321.jobPortalWebsite.crawler.service;

import com.kou5321.jobPortalWebsite.config.RabbitMQConfig;
import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class JobPulseCrawlerService {
    private final RestTemplate restTemplate;
    private final JobPostingRepository jobPostingRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public JobPulseCrawlerService(JobPostingRepository jobPostingRepository, RabbitTemplate rabbitTemplate) {
        this.restTemplate = new RestTemplate();
        this.jobPostingRepository = jobPostingRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void fetchAndSaveJobPostings() {
        int totalJobPostingsToFetch = 300;
        int pageSize = 20;
        int totalPages = totalJobPostingsToFetch / pageSize;

        try {
            log.info("begin web crawler");

            for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
                String apiUrl = "https://job-pulse.uc.r.appspot.com/jobs/sde?yoe_less_than=5&page_number=" + pageNumber + "&page_size=" + pageSize;
                ResponseEntity<List<JobPosting>> responseEntity =
                        restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<JobPosting>>() {});

                List<JobPosting> jobPostings = responseEntity.getBody();
                log.info("Job postings fetched from page {}: {}", pageNumber, jobPostings);

                if (jobPostings != null) {
                    rabbitTemplate.convertAndSend(RabbitMQConfig.JOB_QUEUE, jobPostings);
                }

                // Sleep between requests to avoid hitting the server too hard
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching or saving job postings: ", e);
        }
    }
}
