package com.example.ezback.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Schema(description = "질문 검색 결과 응답")
public class QuestionSearchResultResponse {

    @Schema(description = "검색 결과 목록")
    private List<QuestionSearchItemResponse> results;

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int page;

    @Schema(description = "페이지 크기", example = "20")
    private int size;

    @Schema(description = "전체 페이지 수", example = "3")
    private int totalPages;

    public static QuestionSearchResultResponse from(Page<?> page, List<QuestionSearchItemResponse> results) {
        return new QuestionSearchResultResponse(
                results,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
