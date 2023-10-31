package com.kou5321.jobPortalWebsite.user.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public record SignUpRequest(String email, String username, String password) {}