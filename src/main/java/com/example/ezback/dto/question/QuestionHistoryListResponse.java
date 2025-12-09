package com.example.ezback.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "질문 히스토리 목록 응답")
public class QuestionHistoryListResponse {

    @Schema(description = "질문 히스토리 목록")
    private List<QuestionHistoryResponse> history;
}
