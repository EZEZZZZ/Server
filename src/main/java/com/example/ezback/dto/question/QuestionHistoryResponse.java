package com.example.ezback.dto.question;

import com.example.ezback.entity.UserDailyQuestion;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Schema(description = "질문 히스토리 항목 응답")
public class QuestionHistoryResponse {

    @Schema(description = "질문 ID", example = "101")
    private Long questionId;

    @Schema(description = "질문 내용", example = "오늘 가장 행복했던 순간은?")
    private String question;

    @Schema(description = "질문 날짜", example = "2025-01-30")
    private LocalDate date;

    @Schema(description = "답변 여부", example = "true")
    private Boolean answered;

    @Schema(description = "답변 내용", example = "출근길에 들은 노래가 너무 좋았어!")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answer;

    public static QuestionHistoryResponse from(UserDailyQuestion userDailyQuestion) {
        return new QuestionHistoryResponse(
                userDailyQuestion.getQuestion().getId(),
                userDailyQuestion.getQuestion().getQuestion(),
                userDailyQuestion.getDate(),
                userDailyQuestion.getAnswered(),
                userDailyQuestion.getAnswer()
        );
    }
}
