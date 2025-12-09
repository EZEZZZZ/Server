package com.example.ezback.dto.mission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "미션 히스토리 응답")
public class MissionHistoryResponse {

    @Schema(description = "미션 히스토리 목록")
    private List<MissionHistoryItemResponse> missions;

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int page;

    @Schema(description = "페이지 크기", example = "20")
    private int size;

    @Schema(description = "전체 페이지 수", example = "3")
    private int totalPages;

    public static MissionHistoryResponse from(Page<?> page, List<MissionHistoryItemResponse> missions) {
        return new MissionHistoryResponse(
                missions,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
