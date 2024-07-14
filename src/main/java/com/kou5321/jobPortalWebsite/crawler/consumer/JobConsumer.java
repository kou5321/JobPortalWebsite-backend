package com.kou5321.jobPortalWebsite.crawler.consumer;

import com.kou5321.jobPortalWebsite.config.RabbitMQConfig;
import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JobConsumer {

    private final JobPostingRepository jobPostingRepository;

    @Autowired
    public JobConsumer(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.JOB_QUEUE)
    public void handleJob(List<JobPosting> jobPostings) {
        try {
            log.info("Received job postings: {}", jobPostings);
            jobPostingRepository.saveAll(jobPostings);
            log.info("Successfully saved job postings to the database.");
        } catch (Exception e) {
            log.error("Failed to save job postings: ", e);
        }
    }
}
