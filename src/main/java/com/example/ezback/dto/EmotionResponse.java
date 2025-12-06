package com.example.ezback.dto;

import com.example.ezback.entity.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class EmotionResponse {
    private Long emotionId;
    private Emotion emotion;
    private Integer intensity;
    private String memo;
    private LocalDateTime createdAt;
}
