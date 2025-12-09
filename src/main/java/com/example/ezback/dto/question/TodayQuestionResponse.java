package com.example.ezback.dto.question;

import com.example.ezback.entity.UserDailyQuestion;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Schema(description = "오늘의 질문 응답")
public class TodayQuestionResponse {

    @Schema(description = "질문 ID", example = "101")
    private Long questionId;

    @Schema(description = "질문 내용", example = "최근에 하루 중 가장 행복했던 순간은 뭐야?")
    private String question;

    @Schema(description = "질문 날짜", example = "2025-02-01")
    private LocalDate date;

    @Schema(description = "답변 여부", example = "false")
    private Boolean answered;

    @Schema(description = "답변 내용", example = "아침에 햇살 받으며 커피 마셨을 때!")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answer;

    public static TodayQuestionResponse from(UserDailyQuestion userDailyQuestion) {
        return new TodayQuestionResponse(
                userDailyQuestion.getQuestion().getId(),
                userDailyQuestion.getQuestion().getQuestion(),
                userDailyQuestion.getDate(),
                userDailyQuestion.getAnswered(),
                userDailyQuestion.getAnswered() ? userDailyQuestion.getAnswer() : null
        );
    }
}
