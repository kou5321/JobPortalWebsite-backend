package com.kou5321.jobPortalWebsite.user.repository;

import com.kou5321.jobPortalWebsite.user.entity.EmailSubscription;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSubscriptionRepository extends JpaRepository<EmailSubscription, Long> {
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    void deleteByEmail(String email);
}