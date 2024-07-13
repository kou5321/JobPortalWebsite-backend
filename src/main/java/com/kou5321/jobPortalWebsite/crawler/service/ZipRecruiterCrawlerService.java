package com.kou5321.jobPortalWebsite.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

// TODO： rewrite https://github.com/Bunsly/JobSpy/ code into java
@Slf4j
@Service
public class ZipRecruiterCrawlerService {
    private final RabbitTemplate rabbitTemplate;
    private final ExecutorService taskExecutor;

    @Autowired
    public ZipRecruiterCrawlerService(RabbitTemplate rabbitTemplate, ExecutorService taskExecutor) {
        this.rabbitTemplate = rabbitTemplate;
        this.taskExecutor = taskExecutor;
    }

    public void crawlZipRecruiterJobPostings() {
        taskExecutor.submit(() -> {
            try {
                // 设置Selenium WebDriver
                System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver"); // TODO： change hard-coded position
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");  // 无头模式
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

                WebDriver driver = new ChromeDriver(options);

                driver.get("https://www.ziprecruiter.com/candidate/search?search=software+engineer&location=San+Francisco%2C+CA");

                // 等待页面加载和JavaScript渲染
                TimeUnit.SECONDS.sleep(5);
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
                TimeUnit.SECONDS.sleep(5); // 再次等待，确保内容加载

                // 输出页面内容以进行调试
//                String pageSource = driver.getPageSource();
//                log.info("Page source: {}", pageSource);

                // 验证选择器
                List<WebElement> jobElements = driver.findElements(By.cssSelector(".job_content"));
                log.info("Found {} job postings", jobElements.size());  // 输出找到的职位数量

                for (WebElement jobElement : jobElements) {
                    try {
                        String title = jobElement.findElement(By.cssSelector(".job_title")).getText();
                        String company = jobElement.findElement(By.cssSelector(".company_name")).getText();
                        String location = jobElement.findElement(By.cssSelector(".location")).getText();
                        String dateAdded = jobElement.findElement(By.cssSelector(".posted_date")).getText();
                        String applyLink = jobElement.findElement(By.cssSelector("a")).getAttribute("href");

                        log.info("Job posting: Title={}, Company={}, Location={}, DateAdded={}, ApplyLink={}",
                                title, company, location, dateAdded, applyLink);
                    } catch (Exception e) {
                        log.warn("Failed to parse job element: ", e);
                    }
                }

                driver.quit();
            } catch (Exception e) {
                log.error("An error occurred while fetching ZipRecruiter job postings: ", e);
            }
        });
    }
}
