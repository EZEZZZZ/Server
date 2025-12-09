package com.example.ezback.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "질문 답변 제출 요청")
public class SubmitAnswerRequest {

    @NotNull(message = "질문 ID는 필수입니다.")
    @Schema(description = "질문 ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long questionId;

    @NotBlank(message = "답변은 필수입니다.")
    @Size(max = 300, message = "답변은 최대 300자까지 입력 가능합니다.")
    @Schema(description = "답변 내용", example = "오늘은 커피 마시는 시간이 가장 좋았어!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String answer;

    // TODO: 답변 글자 수 제한 정책 명확화 필요 (명세에서 예시로 300자 제시, 정확한 수치 미명시)
}
