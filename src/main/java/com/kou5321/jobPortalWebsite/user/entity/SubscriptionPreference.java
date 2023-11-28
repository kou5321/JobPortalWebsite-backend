package com.kou5321.jobPortalWebsite.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name = "subscription_preferences")
@Getter
@Setter
@JsonIgnoreProperties("user")
public class SubscriptionPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preferred_year")
    private int preferredYear;

    @Column(name = "preferred_location")
    private String preferredLocation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
