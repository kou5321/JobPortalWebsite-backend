package com.kou5321.jobPortalWebsite.user.service;

import com.kou5321.jobPortalWebsite.email.service.EmailService;
import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.job.repository.JobPostingRepository;
import com.kou5321.jobPortalWebsite.security.JwtTokenProvider;
import com.kou5321.jobPortalWebsite.user.dto.LoginRequest;
import com.kou5321.jobPortalWebsite.user.dto.SignUpRequest;
import com.kou5321.jobPortalWebsite.user.dto.UserLoginResponse;
import com.kou5321.jobPortalWebsite.user.entity.Role;
import com.kou5321.jobPortalWebsite.user.entity.SubscriptionPreference;
import com.kou5321.jobPortalWebsite.user.entity.User;
import com.kou5321.jobPortalWebsite.user.repository.RoleRepository;
import com.kou5321.jobPortalWebsite.user.repository.SubscriptionPreferenceRepository;
import com.kou5321.jobPortalWebsite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final JobPostingRepository jobPostingRepository;
    private final SubscriptionPreferenceRepository subscriptionPreferenceRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

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

    @Transactional
    public UserLoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        User user = getUserByUsername(request.username());

        // 将用户会话信息存储到Redis
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(sessionId, user);

        return new UserLoginResponse(jwt, user);
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

            // update user session information
            String sessionId = "user:" + user.getId().toString();
            redisTemplate.opsForValue().set(sessionId, user);
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

            // update user session information
            String sessionId = "user:" + user.getId().toString();
            redisTemplate.opsForValue().set(sessionId, user);
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
        jobPostingRepository.findById(jobPostingId).orElseThrow(
                () -> new IllegalArgumentException("Job Posting not found with ID: " + jobPostingId)
        );
        user.getViewedJobPostingsIds().add(jobPostingId);
        userRepository.save(user);
        // update user session information
        String sessionId = "user:" + user.getId().toString();
        redisTemplate.opsForValue().set(sessionId, user);
    }

    @Transactional(readOnly = true)
    public Set<JobPosting> getUserViewedJobPostings(UUID userId) {
        User user = getUserById(userId);
        Set<String> viewedJobPostingsIds = user.getViewedJobPostingsIds();
        List<JobPosting> jobPostings = jobPostingRepository.findAllById(viewedJobPostingsIds);
        return new HashSet<>(jobPostings);
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID userId) {
        String sessionId = "user:" + userId.toString();
        User user = (User) redisTemplate.opsForValue().get(sessionId);
        if (user == null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
            // persist user information to redis
            redisTemplate.opsForValue().set(sessionId, user);
        }
        return user;
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

    @Transactional
    public void addSubscriptionPreference(User user, SubscriptionPreference preference) {
        removeAllSubscriptionPreferences(user);

        preference.setUser(user);
        user.getSubscriptionPreferences().add(preference);
        subscriptionPreferenceRepository.save(preference);

        // update user session info to redis
        String sessionId = "user:" + user.getId().toString();
        redisTemplate.opsForValue().set(sessionId, user);
    }

    @Transactional
    public void removeAllSubscriptionPreferences(User user) {
        user.getSubscriptionPreferences().clear();
        userRepository.save(user);

        // update user session info to redis
        String sessionId = "user:" + user.getId().toString();
        redisTemplate.opsForValue().set(sessionId, user);
    }
    public void sendJobAlertsToSubscribedUsers() {
        List<User> users = userRepository.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy"); // Format to match the date part
        String today = sdf.format(new Date());
        String dateRegex = "^" + today;
        for (User user : users) {
            user.getSubscriptionPreferences().forEach(preference -> {
                List<JobPosting> jobPostings = jobPostingRepository.findByDateLocationAndYoe(
                        dateRegex, preference.getPreferredLocation(), preference.getPreferredYear());
                String emailContent = buildEmailContent(jobPostings);
                emailService.sendEmail(user.getEmail(), "Job Alerts", "Here is today's new job post: \n"
                        + emailContent);
            });
        }
    }

    private String buildEmailContent(List<JobPosting> jobPostings) {
        StringBuilder content = new StringBuilder();
        for (JobPosting job : jobPostings) {
            content.append("Title: ").append(job.getTitle())
                    .append(", Company: ").append(job.getCompany())
                    .append(", yoe: ").append(job.getYoe())
                    .append(", apply link: ").append(job.getApply_link())
                    .append("\n\n");
        }
        return content.toString();
    }

}
