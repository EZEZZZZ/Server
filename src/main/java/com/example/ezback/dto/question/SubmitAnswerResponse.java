package com.example.ezback.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "질문 답변 제출 응답")
public class SubmitAnswerResponse {

    @Schema(description = "성공 메시지", example = "답변이 성공적으로 제출되었습니다.")
    private String message;
}
