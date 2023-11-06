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
    private String category;
    @TextIndexed
    private String title;
    @TextIndexed
    private String yoe;
    private String date_added;
    private String location;
    private String sponsor;
    private String apply_link;
}
