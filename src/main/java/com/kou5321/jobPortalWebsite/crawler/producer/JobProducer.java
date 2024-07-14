package com.kou5321.jobPortalWebsite.crawler.producer;

import com.kou5321.jobPortalWebsite.config.RabbitMQConfig;
import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendJobs(List<JobPosting> jobPostings) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.JOB_QUEUE, jobPostings);
    }
}
