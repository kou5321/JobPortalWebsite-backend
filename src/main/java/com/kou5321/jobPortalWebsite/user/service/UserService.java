package com.kou5321.jobPortalWebsite.user.service;

import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import com.kou5321.jobPortalWebsite.user.dto.LoginRequest;
import com.kou5321.jobPortalWebsite.user.dto.SignUpRequest;
import com.kou5321.jobPortalWebsite.user.entity.User;
import com.kou5321.jobPortalWebsite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ComponentScan
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JobPostingRepository jobPostingRepository;

    @Transactional
    public User signUp(SignUpRequest request) {
        validateUserUniqueness(request.email(), request.username());

        User newUser = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();
        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User login(LoginRequest request) {
        return userRepository
                .findByUsername(request.username())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid name or password."));
    }

    @Transactional
    public void markAppliedJobPosting(User user, String jobPostingId) {
        // Check if jobPostingId exists
        jobPostingRepository.findById(jobPostingId).orElseThrow(
                () -> new IllegalArgumentException("Job Posting not found with ID: " + jobPostingId)
        );
        if (!user.getAppliedJobPostingsIds().contains(jobPostingId)) {
            user.getAppliedJobPostingsIds().add(jobPostingId);
            userRepository.save(user);
        }
    }

    @Transactional
    public void unmarkAppliedJobPosting(User user, String jobPostingId) {
        // Check if jobPostingId exists
        jobPostingRepository.findById(jobPostingId).orElseThrow(
                () -> new IllegalArgumentException("Job Posting not found with ID: " + jobPostingId)
        );
        if (user.getAppliedJobPostingsIds().contains(jobPostingId)) {
            user.getAppliedJobPostingsIds().remove(jobPostingId);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public Set<String> getUserAppliedJobPostings(UUID userId) {
        User user = getUserById(userId);
        return user.getAppliedJobPostingsIds();
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    private void validateUserUniqueness(String email, String username) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists.");
        }
    }
}
