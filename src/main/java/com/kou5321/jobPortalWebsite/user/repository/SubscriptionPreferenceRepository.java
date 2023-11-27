package com.kou5321.jobPortalWebsite.user.repository;

import com.kou5321.jobPortalWebsite.user.entity.SubscriptionPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPreferenceRepository extends JpaRepository<SubscriptionPreference, Long> {
}
