package com.kou5321.jobPortalWebsite.user.dto;

import com.kou5321.jobPortalWebsite.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginResponse {
    private String jwtToken;
    private User user;
}
