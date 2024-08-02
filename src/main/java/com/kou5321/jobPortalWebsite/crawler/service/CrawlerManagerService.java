package com.kou5321.jobPortalWebsite.crawler.service;

import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Slf4j
@Service
public class CrawlerManagerService {

    @Autowired
    private GithubCrawlerService githubCrawlerService;

    @Autowired
    private JobPulseCrawlerService jobPulseCrawlerService;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void init() {
        // 创建一个自定义的ScheduledExecutorService
        int corePoolSize = 16;  // 核心线程数
        int maximumPoolSize = 32;  // 最大线程数
        long keepAliveTime = 10;  // 非核心线程的存活时间
        TimeUnit unit = TimeUnit.MINUTES;  // 存活时间的单位
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();  // 工作队列

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue
        );

        scheduledExecutorService = new ScheduledThreadPoolExecutor(corePoolSize, threadPoolExecutor.getThreadFactory()) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                super.beforeExecute(t, r);
                log.info("Before Execute: " + t.getName());
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                log.info("After Execute");
                if (t != null) {
                    log.error("Error in thread execution: ", t);
                }
            }
        };

        // 配置并启动定时任务
        long initialDelay = 0; // 初始延迟
        long period = 12; // 任务周期，单位为小时
        scheduledExecutorService.scheduleAtFixedRate(this::runCrawlers, initialDelay, period, TimeUnit.HOURS);
    }

    public void runCrawlers() {
        try {
            // 清空数据库
            log.info("Deleting all existing job postings");
            jobPostingRepository.deleteAll();

            // 创建一个线程池来并行执行爬虫任务
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            // 运行 GitHub 爬虫
            executorService.submit(() -> {
                try {
                    log.info("Running GitHub Crawler");
                    githubCrawlerService.crawlGitHubJobPostings();
                } catch (Exception e) {
                    log.error("An error occurred while running GitHub Crawler: ", e);
                }
            });

            // 运行 JobPulse 爬虫
            executorService.submit(() -> {
                try {
                    log.info("Running JobPulse Crawler");
                    jobPulseCrawlerService.fetchAndSaveJobPostings();
                } catch (Exception e) {
                    log.error("An error occurred while running JobPulse Crawler: ", e);
                }
            });

            // 关闭线程池
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);

        } catch (Exception e) {
            log.error("An error occurred while running crawlers: ", e);
        }
    }
}
