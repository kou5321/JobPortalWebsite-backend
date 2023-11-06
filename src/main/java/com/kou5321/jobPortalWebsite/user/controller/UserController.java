package com.kou5321.jobPortalWebsite.user.controller;

import com.kou5321.jobPortalWebsite.user.dto.LoginRequest;
import com.kou5321.jobPortalWebsite.user.dto.SignUpRequest;
import com.kou5321.jobPortalWebsite.user.entity.User;
import com.kou5321.jobPortalWebsite.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

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

    @PostMapping("/{userId}/apply-job-posting")
    public ResponseEntity<String> markAppliedJobPosting(@PathVariable UUID userId, @RequestBody String jobPostingId) {
        User user = userService.getUserById(userId);
        userService.markAppliedJobPosting(user, jobPostingId);
        return ResponseEntity.ok("Job Posting marked as applied successfully.");
    }

    @DeleteMapping("/{userId}/unapply-job-posting")
    public ResponseEntity<String> unmarkAppliedJobPosting(@PathVariable UUID userId, @RequestBody String jobPostingId) {
        User user = userService.getUserById(userId);
        userService.unmarkAppliedJobPosting(user, jobPostingId);
        return ResponseEntity.ok("Job Posting unmarked as applied successfully.");
    }

    // TODO: Implement this function
    @GetMapping("/{userId}/get-user-applied-list")
    public ResponseEntity<String> getUserAppliedList(@PathVariable UUID userId) {
        return null;
    }
}
