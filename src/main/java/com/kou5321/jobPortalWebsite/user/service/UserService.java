package com.kou5321.jobPortalWebsite.user.service;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import com.kou5321.jobPortalWebsite.security.JwtTokenProvider;
import com.kou5321.jobPortalWebsite.user.dto.LoginRequest;
import com.kou5321.jobPortalWebsite.user.dto.SignUpRequest;
import com.kou5321.jobPortalWebsite.user.entity.Role;
import com.kou5321.jobPortalWebsite.user.entity.User;
import com.kou5321.jobPortalWebsite.user.repository.RoleRepository;
import com.kou5321.jobPortalWebsite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final JobPostingRepository jobPostingRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public User signUp(SignUpRequest request) {
        validateUserUniqueness(request.email(), request.username());

        User newUser = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(Collections.singleton(getUserRole())) // Set default role
                .build();

        return userRepository.save(newUser);
    }

    private Role getUserRole() {
        return roleRepository.findByName("USER") // Replace with your role name
                .orElseThrow(() -> new IllegalStateException("User role not found."));
    }

//    @Transactional(readOnly = true)
//    public User login(LoginRequest request) {
//        return userRepository
//                .findByUsername(request.username())
//                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
//                .orElseThrow(() -> new IllegalArgumentException("Invalid name or password."));
//    }

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
    public Set<JobPosting> getUserAppliedJobPostings(UUID userId) {
        User user = getUserById(userId);
        Set<String> appliedJobPostingsIds = user.getAppliedJobPostingsIds();
        List<JobPosting> jobPostings = jobPostingRepository.findAllById(appliedJobPostingsIds);
        return new HashSet<>(jobPostings);
    }

    @Transactional
    public void markViewedJobPosting(User user, String jobPostingId) {
        // TODO: also check if jobPostingId exists in the JobPostingRepository like in markAppliedJobPosting
        user.getViewedJobPostingsIds().add(jobPostingId);
        userRepository.save(user);
    }

    // not useful for business logic
//    @Transactional
//    public void unmarkViewedJobPosting(User user, String jobPostingId) {
//        user.getViewedJobPostingsIds().remove(jobPostingId);
//        userRepository.save(user);
//    }

    @Transactional(readOnly = true)
    public Set<JobPosting> getUserViewedJobPostings(UUID userId) {
        User user = getUserById(userId);
        Set<String> viewedJobPostingsIds = user.getViewedJobPostingsIds();
        List<JobPosting> jobPostings = jobPostingRepository.findAllById(viewedJobPostingsIds);
        return new HashSet<>(jobPostings);
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

    private void validateUserUniqueness(String email, String username) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists.");
        }
    }

    // In UserService
    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

}
