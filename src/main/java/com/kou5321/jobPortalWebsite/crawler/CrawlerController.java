package com.kou5321.jobPortalWebsite.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlerController {
    @Autowired
    GithubCrawlerService githubCrawlerService;
    @Autowired
    JobPulseCrawlerService jobPulseCrawlerService;
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
