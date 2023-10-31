package com.kou5321.jobPortalWebsite.user.service;

import com.kou5321.jobPortalWebsite.user.dto.SignUpRequest;
import com.kou5321.jobPortalWebsite.user.entity.User;
import com.kou5321.jobPortalWebsite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email `%s` is already exists.".formatted(request.email()));
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username `%s` is already exists.".formatted(request.username()));
        }

        User newUser = this.createNewUser(request);
        return userRepository.save(newUser);
    }

    // TODO: encrypt the password
    private User createNewUser(SignUpRequest request) {
        return User.builder()
                .email(request.email())
                .username(request.username())
                .password(request.password())
                .build();
    }
}
