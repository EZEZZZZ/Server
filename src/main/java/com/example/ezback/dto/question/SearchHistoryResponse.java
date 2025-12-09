package com.example.ezback.dto.question;

import com.example.ezback.entity.SearchHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "검색 히스토리 응답")
public class SearchHistoryResponse {

    @Schema(description = "히스토리 ID", example = "12")
    private Long historyId;

    @Schema(description = "검색 키워드", example = "행복")
    private String keyword;

    @Schema(description = "검색 시각", example = "2025-02-01T10:20:00")
    private LocalDateTime createdAt;

    public static SearchHistoryResponse from(SearchHistory searchHistory) {
        return new SearchHistoryResponse(
                searchHistory.getId(),
                searchHistory.getKeyword(),
                searchHistory.getCreatedAt()
        );
    }
}
