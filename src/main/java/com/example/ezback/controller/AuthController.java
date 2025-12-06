package com.example.ezback.controller;

import com.example.ezback.dto.LogoutRequest;
import com.example.ezback.dto.LogoutResponse;
import com.example.ezback.exception.InvalidRequestException;
import com.example.ezback.exception.InvalidTokenException;
import com.example.ezback.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request) {
        if (request.getAccessToken() == null || request.getAccessToken().trim().isEmpty()) {
            throw new InvalidRequestException("유효한 요청이 아닙니다.");
        }

        if (!jwtUtil.validateToken(request.getAccessToken())) {
            throw new InvalidTokenException("로그인 정보가 유효하지 않습니다.");
        }

        return ResponseEntity.ok(new LogoutResponse("로그아웃 되었습니다."));
    }
}