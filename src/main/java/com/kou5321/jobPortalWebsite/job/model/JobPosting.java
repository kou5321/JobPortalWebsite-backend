package com.kou5321.jobPortalWebsite.job.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "JobCrawler")
public class JobPosting {
    @Id
    private String id;
    @TextIndexed
    private String company;
    @TextIndexed
    private String category;
    @TextIndexed
    private String title;
    @TextIndexed
    private int yoe;
    private String date_added;
    @TextIndexed
    private String location;
    private String sponsor;
    private String apply_link;
}
