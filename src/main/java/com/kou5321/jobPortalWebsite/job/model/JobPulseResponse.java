package com.kou5321.jobPortalWebsite.job.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPulseResponse {
    private List<JobPosting> data;
}
