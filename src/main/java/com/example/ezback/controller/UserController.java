package com.example.ezback.controller;

import com.example.ezback.dto.UserInfoResponse;
import com.example.ezback.entity.User;
import com.example.ezback.exception.UnauthorizedException;
import com.example.ezback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        User user = (User) authentication.getPrincipal();
        UserInfoResponse response = userService.getCurrentUserInfo(user);

        return ResponseEntity.ok(response);
    }
}
