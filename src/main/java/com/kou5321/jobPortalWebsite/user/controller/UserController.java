package com.kou5321.jobPortalWebsite.user.controller;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.user.dto.LoginRequest;
import com.kou5321.jobPortalWebsite.user.dto.SignUpRequest;
import com.kou5321.jobPortalWebsite.user.entity.User;
import com.kou5321.jobPortalWebsite.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users") // Added a request mapping to set a base path for all endpoints in this controller
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest request) {
        User newUser = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        User loggedInUser = userService.login(request);
        return ResponseEntity.ok(loggedInUser);
    }

    @PostMapping("/{userId}/mark-applied-job")
    public ResponseEntity<String> markAppliedJobPosting(@PathVariable UUID userId, @RequestParam String jobPostingId) {
        User user = userService.getUserById(userId);
        userService.markAppliedJobPosting(user, jobPostingId);
        return ResponseEntity.ok("Job Posting marked as applied successfully.");
    }

    @DeleteMapping("/{userId}/unmark-applied-job")
    public ResponseEntity<String> unmarkAppliedJobPosting(@PathVariable UUID userId, @RequestParam String jobPostingId) {
        User user = userService.getUserById(userId);
        userService.unmarkAppliedJobPosting(user, jobPostingId);
        return ResponseEntity.ok("Job Posting unmarked as applied successfully.");
    }

    @GetMapping("/{userId}/get-user-applied-list")
    public ResponseEntity<Set<JobPosting>> getUserAppliedList(@PathVariable UUID userId) {
        Set<JobPosting> appliedJobPostings = userService.getUserAppliedJobPostings(userId);
        return ResponseEntity.ok(appliedJobPostings);
    }

    @PostMapping("/{userId}/mark-viewed-job")
    public ResponseEntity<String> markViewedJobPosting(@PathVariable UUID userId, @RequestParam String jobPostingId) {
        User user = userService.getUserById(userId);
        userService.markViewedJobPosting(user, jobPostingId);
        return ResponseEntity.ok("Job Posting marked as viewed successfully.");
    }

    @DeleteMapping("/{userId}/unmark-viewed-job")
    public ResponseEntity<String> unmarkViewedJobPosting(@PathVariable UUID userId, @RequestParam String jobPostingId) {
        User user = userService.getUserById(userId);
        userService.unmarkViewedJobPosting(user, jobPostingId);
        return ResponseEntity.ok("Job Posting unmarked as viewed successfully.");
    }

    @GetMapping("/{userId}/get-user-viewed-list")
    public ResponseEntity<Set<JobPosting>> getUserViewedList(@PathVariable UUID userId) {
        Set<JobPosting> viewedJobPostings = userService.getUserViewedJobPostings(userId);
        return ResponseEntity.ok(viewedJobPostings);
    }
}
