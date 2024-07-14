package com.kou5321.jobPortalWebsite.crawler.service;

import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CrawlerManagerService {

    @Autowired
    private GithubCrawlerService githubCrawlerService;

    @Autowired
    private JobPulseCrawlerService jobPulseCrawlerService;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void scheduleTasks() {
        scheduledExecutorService.scheduleAtFixedRate(this::runCrawlers, 0, 12, TimeUnit.HOURS);
    }

    public void runCrawlers() {
        try {
            // 清空数据库
            log.info("Deleting all existing job postings");
            jobPostingRepository.deleteAll();

            // 运行 GitHub 爬虫
            log.info("Running GitHub Crawler");
            githubCrawlerService.crawlGitHubJobPostings();

            // 运行 JobPulse 爬虫
            log.info("Running JobPulse Crawler");
            jobPulseCrawlerService.fetchAndSaveJobPostings();

        } catch (Exception e) {
            log.error("An error occurred while running crawlers: ", e);
        }
    }
}
