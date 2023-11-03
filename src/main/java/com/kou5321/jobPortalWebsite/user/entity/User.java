package com.kou5321.jobPortalWebsite.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Getter
@Builder
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
