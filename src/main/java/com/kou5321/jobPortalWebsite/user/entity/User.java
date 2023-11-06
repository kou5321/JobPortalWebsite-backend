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
@Builder
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Builder.Default
    @Column(length = 500, nullable = false)
    private String bio = "This one is too lazy and haven't leave anything yet...";
    // TODO: The Image currently doesn't get stored
    private String image = "https://img.moegirl.org.cn/common/b/b7/Transparent_Akkarin.jpg";

    @ElementCollection
    @CollectionTable(name = "user_applied_job_postings", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "job_posting_id")
    private Set<String> appliedJobPostingsIds = new HashSet<>();

    @Override
    // TODO: remove the toString in the future because of the password
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
