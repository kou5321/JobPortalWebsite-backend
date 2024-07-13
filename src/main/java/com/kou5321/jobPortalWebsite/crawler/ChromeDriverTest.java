package com.kou5321.jobPortalWebsite.crawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverTest {
    public static void main(String[] args) {
        // 设置ChromeDriver的路径
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");  // 替换为实际路径

        // 配置Chrome选项
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // 可选，无头模式
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // 启动ChromeDriver
        WebDriver driver = new ChromeDriver(options);

        // 访问一个网站
        driver.get("https://www.google.com");

        // 获取并打印页面标题
        String pageTitle = driver.getTitle();
        System.out.println("Page title is: " + pageTitle);

        // 关闭浏览器
        driver.quit();
    }
}
