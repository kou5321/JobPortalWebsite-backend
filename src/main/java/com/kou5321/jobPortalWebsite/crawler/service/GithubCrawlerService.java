package com.kou5321.jobPortalWebsite.crawler.service;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
@Service
@EnableScheduling
public class GithubCrawlerService {

    private final JobPostingRepository jobPostingRepository;

    public GithubCrawlerService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    // 每6小时执行一次
    @Scheduled(cron = "0 0 */6 * * ?")
    public void crawlGitHubJobPostings() {
        try {
            log.info("Deleting all existing job postings");
            jobPostingRepository.deleteAll();

            // 等待一秒钟以确保删除操作完成
            Thread.sleep(1000);
            log.info("begin web crawler");
            String GITHUB_URL = "https://github.com/ReaVNaiL/New-Grad-2024"; // 确保这是正确的URL
            Document doc = Jsoup.connect(GITHUB_URL).get();

            // 查找HTML中的表格元素
            Elements jobElements = doc.select("table");

            // 迭代表格中的每一行
            for (Element row : jobElements.select("tr")) {
                Elements cells = row.select("td");
                log.info("Processing row: " + row.text());

                if (cells.size() >= 4) { // 确保有足够的列
                    String name = cells.get(0).text();
                    String roles = cells.get(2).text();
                    String link = cells.get(2).select("a").attr("href");
                    String sponsor = cells.get(3).text();

                    // 创建JobPosting对象并保存
                    JobPosting jobPosting = new JobPosting();
                    jobPosting.setCompany(name);
                    jobPosting.setTitle(roles);
                    jobPosting.setSponsor(sponsor);
                    jobPosting.setApply_link(link);
                    jobPosting.setYoe(0);
                    jobPosting.setLocation("United States");

                    jobPostingRepository.save(jobPosting);
                } else {
                    log.warn("Row skipped due to insufficient columns: " + row.text());
                }
            }

        } catch (Exception e) {
            log.error("Error occurred while crawling GitHub job postings", e);
        }
    }
}