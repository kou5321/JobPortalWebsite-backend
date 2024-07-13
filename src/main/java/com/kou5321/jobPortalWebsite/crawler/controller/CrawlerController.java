package com.kou5321.jobPortalWebsite.crawler.controller;

import com.kou5321.jobPortalWebsite.crawler.service.GithubCrawlerService;
import com.kou5321.jobPortalWebsite.crawler.service.JobPulseCrawlerService;
import com.kou5321.jobPortalWebsite.crawler.service.LinkedInCrawlerService;
import com.kou5321.jobPortalWebsite.crawler.service.ZipRecruiterCrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlerController {
    private final GithubCrawlerService githubCrawlerService;
    private final JobPulseCrawlerService jobPulseCrawlerService;
    private final LinkedInCrawlerService linkedInCrawlerService;
    private final ZipRecruiterCrawlerService zipRecruiterCrawlerService;

    public CrawlerController(GithubCrawlerService githubCrawlerService,
                             JobPulseCrawlerService jobPulseCrawlerService,
                             LinkedInCrawlerService linkedInCrawlerService,
                             ZipRecruiterCrawlerService zipRecruiterCrawlerService) {
        this.githubCrawlerService = githubCrawlerService;
        this.jobPulseCrawlerService = jobPulseCrawlerService;
        this.linkedInCrawlerService = linkedInCrawlerService;
        this.zipRecruiterCrawlerService = zipRecruiterCrawlerService;
    }

    @GetMapping("/zipRecruiterCrawler")
    public ResponseEntity<String> zipRecruiterCrawler() {
        zipRecruiterCrawlerService.crawlZipRecruiterJobPostings();
        return ResponseEntity.ok("ZipRecruiter job postings have been crawled and sent to queue.");
    }

    @GetMapping("/linkedinCrawler")
    public ResponseEntity<String> linkedInCrawler() {
        linkedInCrawlerService.crawlLinkedInJobPostings();
        return ResponseEntity.ok("LinkedIn job postings have been crawled and sent to queue.");
    }

    @GetMapping("/githubCrawler")
    public ResponseEntity<String> githubCrawler() {
        githubCrawlerService.crawlGitHubJobPostings();
        return ResponseEntity.ok("Job postings have been crawled and saved.");
    }

    @GetMapping("/JobPulseCrawler")
    public ResponseEntity<String> jobPulseCrawler() {
        jobPulseCrawlerService.fetchAndSaveJobPostings();
        return ResponseEntity.ok("Job postings have been crawled and saved.");
    }
}
