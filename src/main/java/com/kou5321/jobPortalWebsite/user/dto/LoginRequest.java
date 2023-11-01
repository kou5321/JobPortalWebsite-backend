package com.kou5321.jobPortalWebsite.user.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public record LoginRequest(String username, String password) {}
