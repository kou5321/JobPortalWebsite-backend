package com.kou5321.jobPortalWebsite.job.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "JobCrawler")
public class JobPosting {
    @TextIndexed
    private String company;
    @TextIndexed
    private String jobTitle;
    @TextIndexed
    private String exp = "?";
    private String sponsor;
    private String link;

    @Override
    public String toString() {
        return "JobPosting{" +
                "company='" + company + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", exp='" + exp + '\'' +
                ", sponsor='" + sponsor + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
