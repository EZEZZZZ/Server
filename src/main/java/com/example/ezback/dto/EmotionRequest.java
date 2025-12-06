package com.example.ezback.dto;

import com.example.ezback.entity.Emotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionRequest {
    private Emotion emotion;
    private Integer intensity;
    private String memo;
}
