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
    public User signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request);
        return userService.signUp(request);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/{userId}/apply-job-posting")
    public ResponseEntity<String> markAppliedJobPosting(@PathVariable UUID userId, @RequestBody String jobPostingId) {
        User user = userService.getUserById(userId);
        userService.markAppliedJobPosting(user, jobPostingId);
        return ResponseEntity.ok("Job Posting marked as applied successfully.");
    }

    // Endpoint to unmark a job posting as applied
    @DeleteMapping("/{userId}/unapply-job-posting")
    public ResponseEntity<String> unmarkAppliedJobPosting(@PathVariable UUID userId, @RequestBody String jobPostingId) {
        User user = userService.getUserById(userId);
        userService.unmarkAppliedJobPosting(user, jobPostingId);
        return ResponseEntity.ok("Job Posting unmarked as applied successfully.");
    }
}
