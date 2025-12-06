package com.example.ezback.service;

import com.example.ezback.dto.EmotionRequest;
import com.example.ezback.dto.EmotionResponse;
import com.example.ezback.entity.User;
import com.example.ezback.entity.UserEmotion;
import com.example.ezback.exception.InvalidRequestException;
import com.example.ezback.repository.UserEmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final UserEmotionRepository userEmotionRepository;

    @Transactional
    public EmotionResponse recordEmotion(User user, EmotionRequest request) {
        if (request.getEmotion() == null) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }

        if (request.getIntensity() == null || request.getIntensity() < 1 || request.getIntensity() > 5) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }

        UserEmotion userEmotion = UserEmotion.builder()
                .user(user)
                .emotion(request.getEmotion())
                .intensity(request.getIntensity())
                .memo(request.getMemo())
                .build();

        UserEmotion savedEmotion = userEmotionRepository.save(userEmotion);

        return EmotionResponse.builder()
                .emotionId(savedEmotion.getId())
                .emotion(savedEmotion.getEmotion())
                .intensity(savedEmotion.getIntensity())
                .memo(savedEmotion.getMemo())
                .createdAt(savedEmotion.getCreatedAt())
                .build();
    }
}
