package com.kou5321.jobPortalWebsite.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subscription_preferences")
@Getter
@Setter
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
    private User user;
}
