package com.example.ezback.controller;

import com.example.ezback.dto.EmotionRequest;
import com.example.ezback.dto.EmotionResponse;
import com.example.ezback.entity.User;
import com.example.ezback.exception.UnauthorizedException;
import com.example.ezback.service.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emotions")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @PostMapping
    public ResponseEntity<EmotionResponse> recordEmotion(
            @RequestBody EmotionRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        User user = (User) authentication.getPrincipal();
        EmotionResponse response = emotionService.recordEmotion(user, request);

        return ResponseEntity.ok(response);
    }
}
