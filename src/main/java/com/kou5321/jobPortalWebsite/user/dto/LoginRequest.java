package com.kou5321.jobPortalWebsite.user.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;

@JsonRootName("user")
public record LoginRequest(String username, String password) {}
