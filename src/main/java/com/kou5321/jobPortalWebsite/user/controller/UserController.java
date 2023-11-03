package com.kou5321.jobPortalWebsite.user.controller;

import com.kou5321.jobPortalWebsite.user.dto.LoginRequest;
import com.kou5321.jobPortalWebsite.user.dto.SignUpRequest;
import com.kou5321.jobPortalWebsite.user.entity.User;
import com.kou5321.jobPortalWebsite.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/users")
    public ModelAndView signUp(@RequestBody SignUpRequest request, HttpServletRequest httpServletRequest) {
        userService.signUp(request);

        // Redirect to login API to automatically login when signup is complete
        LoginRequest loginRequest = new LoginRequest(request.email(), request.password());
        // http response is 307 Temporary Redirect
        httpServletRequest.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/api/users/login", "user", Map.of("user", loginRequest));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/api/users/login")
    public User login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
