package com.kou5321.jobPortalWebsite.crawler.service;

import com.kou5321.jobPortalWebsite.crawler.producer.JobProducer;
import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GithubCrawlerService {

    @Autowired
    private JobProducer jobProducer;

    public void crawlGitHubJobPostings() {
        try {
            log.info("begin web crawler");
            String GITHUB_URL = "https://github.com/ReaVNaiL/New-Grad-2024";
            Document doc = Jsoup.connect(GITHUB_URL).get();

            Elements jobElements = doc.select("table");

            List<JobPosting> jobPostings = new ArrayList<>();
            for (Element row : jobElements.select("tr")) {
                Elements cells = row.select("td");
                log.info("Processing row: " + row.text());

                if (cells.size() >= 4) {
                    String name = cells.get(0).text();
                    String roles = cells.get(2).text();
                    String link = cells.get(2).select("a").attr("href");
                    String sponsor = cells.get(3).text();

                    JobPosting jobPosting = new JobPosting();
                    jobPosting.setCompany(name);
                    jobPosting.setTitle(roles);
                    jobPosting.setSponsor(sponsor);
                    jobPosting.setApply_link(link);
                    jobPosting.setYoe(0);

                    jobPostings.add(jobPosting);
                } else {
                    log.warn("Row skipped due to insufficient columns: " + row.text());
                }
            }

            if (!jobPostings.isEmpty()) {
                jobProducer.sendJobs(jobPostings);
            }

        } catch (Exception e) {
            log.error("Error occurred while crawling GitHub job postings", e);
        }
    }
}
