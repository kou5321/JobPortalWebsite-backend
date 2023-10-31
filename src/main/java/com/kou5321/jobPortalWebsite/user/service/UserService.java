package com.kou5321.jobPortalWebsite.user.service;

import com.kou5321.jobPortalWebsite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
}
