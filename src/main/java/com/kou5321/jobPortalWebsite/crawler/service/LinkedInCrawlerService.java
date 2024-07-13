package com.kou5321.jobPortalWebsite.crawler.service;

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
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class LinkedInCrawlerService {
    private final RestTemplate restTemplate;
    private final JobPostingRepository jobPostingRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ExecutorService taskExecutor;

    @Autowired
    public LinkedInCrawlerService(JobPostingRepository jobPostingRepository, RabbitTemplate rabbitTemplate, ExecutorService taskExecutor) {
        this.restTemplate = new RestTemplate();
        this.jobPostingRepository = jobPostingRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.taskExecutor = taskExecutor;
    }

    public void crawlLinkedInJobPostings() {
        taskExecutor.submit(() -> {
            try {
                // LinkedIn爬取逻辑
                String apiUrl = "https://www.linkedin.com/jobs-guest/jobs/api/jobPosting/3975725317?refId=FHIn3waqpraJeZ9oxG4hNA%3D%3D&trackingId=%2BIbtqrSmdpW37FMp1l5XVQ%3D%3D"; // 示例URL
                ResponseEntity<List<JobPosting>> responseEntity =
                        restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<JobPosting>>() {});

                List<JobPosting> jobPostings = responseEntity.getBody();
                log.info("Job postings fetched from LinkedIn: {}", jobPostings);

                if (jobPostings != null) {
                    rabbitTemplate.convertAndSend("jobQueue", jobPostings);
                }
            } catch (Exception e) {
                log.error("An error occurred while fetching or saving LinkedIn job postings: ", e);
            }
        });
    }
}
