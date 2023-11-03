package com.kou5321.jobPortalWebsite.job.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "JobPost")
public class Post {
    @TextIndexed
    private String profile;
    @TextIndexed
    private String desc;
    private int exp;
    @TextIndexed
    private String[] techs;

    @Override
    public String toString() {
        return "Post{" +
                "profile='" + profile + '\'' +
                ", desc='" + desc + '\'' +
                ", exp=" + exp +
                ", techs=" + Arrays.toString(techs) +
                '}';
    }
}
