package com.kou5321.jobPortalWebsite.email.controller;

import com.kou5321.jobPortalWebsite.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/send-job-alerts")
    public ResponseEntity<?> sendJobAlerts() {
        userService.sendJobAlertsToSubscribedUsers();
        return ResponseEntity.ok("Job alerts sent");
    }
}