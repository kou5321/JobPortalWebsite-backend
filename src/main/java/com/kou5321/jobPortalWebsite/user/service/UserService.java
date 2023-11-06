package com.kou5321.jobPortalWebsite.user.service;

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

import java.util.UUID;

@Service
@RequiredArgsConstructor
@ComponentScan
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email `%s` is already exists.".formatted(request.email()));
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username `%s` is already exists.".formatted(request.username()));
        }

        User newUser = this.createNewUser(request);
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
        if (!user.getAppliedJobPostingsIds().contains(jobPostingId)) {
            user.getAppliedJobPostingsIds().add(jobPostingId);
            userRepository.save(user);
        }
    }

    @Transactional
    public void unmarkAppliedJobPosting(User user, String jobPostingId) {
        if (user.getAppliedJobPostingsIds().contains(jobPostingId)) {
            user.getAppliedJobPostingsIds().remove(jobPostingId);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    private User createNewUser(SignUpRequest request) {
        return User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }
}
