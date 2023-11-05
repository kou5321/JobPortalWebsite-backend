package com.kou5321.jobPortalWebsite.crawler;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

@Slf4j
@Service
public class JobDataCrawlerService {
    private static final String GITHUB_URL = "https://raw.githubusercontent.com/ReaVNaiL/New-Grad-2024/main/README.md";

    @Autowired
    private JobPostingRepository jobPostingRepository;

    public void crawlGitHubJobPostings() {
        try {
            log.info("begin web crawler");
            String GITHUB_URL = "https://github.com/ReaVNaiL/New-Grad-2024"; // Make sure this is the correct URL
            Document doc = Jsoup.connect(GITHUB_URL).get();

            // Look for the table element within the HTML
            Elements jobElements = doc.select("table");
            // this one is correct
//            log.info("jobElements" + jobElements.text()); //

// Iterate over each row in the table
            for (Element row : jobElements.select("tr")) {
//                log.info("row" + row.text());
                Elements cells = row.select("td");
                if (cells.size() > 0) {
                    // Assuming the structure of the table is known and consistent
                    String name = cells.get(0).text();
                    String roles = cells.get(1).text();
                    String link = cells.get(1).select("a").attr("href");
                    String requirements = cells.get(2).text();

                    // Here you would create your JobPosting object and save it
                    JobPosting jobPosting = new JobPosting();
                    jobPosting.setCompany(name);
                    jobPosting.setJobTitle(roles);
                    jobPosting.setSponsor(requirements);
                    jobPosting.setLink(link);

                    // Save each job posting
                    jobPostingRepository.save(jobPosting);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
