package com.kou5321.jobPortalWebsite.user.controller;

import com.kou5321.jobPortalWebsite.job.model.JobPosting;
import com.kou5321.jobPortalWebsite.user.dto.LoginRequest;
import com.kou5321.jobPortalWebsite.user.dto.SignUpRequest;
import com.kou5321.jobPortalWebsite.user.dto.UserLoginResponse;
import com.kou5321.jobPortalWebsite.user.entity.EmailSubscription;
import com.kou5321.jobPortalWebsite.user.entity.SubscriptionPreference;
import com.kou5321.jobPortalWebsite.user.entity.User;
import com.kou5321.jobPortalWebsite.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kou5321.jobPortalWebsite.user.dto.JwtAuthenticationResponse;


import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
@RequestMapping("/users") // Added a request mapping to set a base path for all endpoints in this controller
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest request) {
        User newUser = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
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

    // not useful for the business logic
//    @DeleteMapping("/{userId}/unmark-viewed-job")
//    public ResponseEntity<String> unmarkViewedJobPosting(@PathVariable UUID userId, @RequestParam String jobPostingId) {
//        User user = userService.getUserById(userId);
//        userService.unmarkViewedJobPosting(user, jobPostingId);
//        return ResponseEntity.ok("Job Posting unmarked as viewed successfully.");
//    }

    @GetMapping("/{userId}/get-user-viewed-list")
    public ResponseEntity<Set<JobPosting>> getUserViewedList(@PathVariable UUID userId) {
        Set<JobPosting> viewedJobPostings = userService.getUserViewedJobPostings(userId);
        return ResponseEntity.ok(viewedJobPostings);
    }

    @GetMapping("/get-by-username/{userName}")
    public ResponseEntity<User> getUserInfoByUserName(@PathVariable String userName) {
        User userInfo = userService.getUserByUsername(userName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userInfo);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(
            @RequestParam String username,
            @RequestParam String preferredLocation,
            @RequestParam int preferredYear) {
        try {
            User user = userService.getUserByUsername(username);
            SubscriptionPreference preference = new SubscriptionPreference();
            preference.setPreferredLocation(preferredLocation);
            preference.setPreferredYear(preferredYear);
            userService.addSubscriptionPreference(user, preference);
            return ResponseEntity.ok("Subscribed successfully");
        } catch (Exception e) {
            log.error("Error in subscribing user: " + username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in subscription process");
        }
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestParam String username) {
        try {
            User user = userService.getUserByUsername(username);
            userService.removeAllSubscriptionPreferences(user);
            return ResponseEntity.ok("Unsubscribed successfully");
        } catch (Exception e) {
            log.error("Error in unsubscribing user: " + username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in unsubscription process");
        }
    }

}
