package com.kou5321.jobPortalWebsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "JobPost")
public class Post {
    private String profile;
    private String desc;
    private int exp;
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
