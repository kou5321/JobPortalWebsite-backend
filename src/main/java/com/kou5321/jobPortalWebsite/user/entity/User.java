package com.kou5321.jobPortalWebsite.user.entity;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @ElementCollection
    @CollectionTable(name = "user_applied_job_postings", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "job_posting_id")
    private Set<String> appliedJobPostingsIds = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + "password" + '\'' + // cannot print the password
                ", username='" + username + '\'' +
                ", appliedJobPostingsIds=" + appliedJobPostingsIds +
                '}';
    }
}
