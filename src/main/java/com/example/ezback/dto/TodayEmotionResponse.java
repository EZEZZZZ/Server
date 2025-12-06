package com.example.ezback.dto;

import com.example.ezback.entity.Emotion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TodayEmotionResponse {
    private boolean recorded;
    private Long emotionId;
    private Emotion emotion;
    private Integer intensity;
    private String memo;
    private LocalDateTime createdAt;

    public TodayEmotionResponse(boolean recorded) {
        this.recorded = recorded;
    }
}
