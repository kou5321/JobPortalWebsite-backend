package com.kou5321.jobPortalWebsite.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlerController {
    private final GithubCrawlerService githubCrawlerService;
    private final JobPulseCrawlerService jobPulseCrawlerService;
    private final LinkedInCrawlerService linkedInCrawlerService;

    public CrawlerController(GithubCrawlerService githubCrawlerService, JobPulseCrawlerService jobPulseCrawlerService, LinkedInCrawlerService linkedInCrawlerService) {
        this.githubCrawlerService = githubCrawlerService;
        this.jobPulseCrawlerService = jobPulseCrawlerService;
        this.linkedInCrawlerService = linkedInCrawlerService;
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
