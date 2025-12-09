package com.example.ezback.dto.question;

import com.example.ezback.entity.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "질문 검색 결과 항목")
public class QuestionSearchItemResponse {

    @Schema(description = "질문 ID", example = "101")
    private Long questionId;

    @Schema(description = "질문 내용", example = "최근 가장 행복했던 순간은?")
    private String question;

    // TODO: category 필드 추가 필요 (현재 Question 엔티티에 category 필드 없음, 명세에서 "존재할 경우 반환"으로 선택적 명시)
    @Schema(description = "질문 카테고리", example = "감정")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;

    // TODO: usedCount 필드 추가 필요 (현재 Question 엔티티에 usedCount 필드 없음, 명세에서 "존재할 경우 반환"으로 선택적 명시)
    // usedCount는 UserDailyQuestion 테이블에서 question_id별로 COUNT 집계 필요
    @Schema(description = "질문 사용 횟수", example = "12")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer usedCount;

    public static QuestionSearchItemResponse from(Question question) {
        return new QuestionSearchItemResponse(
                question.getId(),
                question.getQuestion(),
                null,  // TODO: category 필드가 엔티티에 추가되면 question.getCategory()로 변경
                null   // TODO: usedCount 계산 로직 추가 필요
        );
    }
}
