package com.example.ezback.dto.mission;

import com.example.ezback.entity.MissionType;
import com.example.ezback.entity.UserMission;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "미션 히스토리 항목 응답")
public class MissionHistoryItemResponse {

    @Schema(description = "미션 ID", example = "42")
    private Long missionId;

    @Schema(description = "미션 제목", example = "서로 칭찬 1개씩 하기")
    private String title;

    @Schema(description = "미션 타입", example = "ACTION")
    private MissionType type;

    @Schema(description = "미션 날짜", example = "2025-02-01")
    private LocalDate date;

    @Schema(description = "완료 여부", example = "true")
    private Boolean performed;

    @Schema(description = "완료 시각", example = "2025-02-01T18:20:00")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime performedAt;

    public static MissionHistoryItemResponse from(UserMission userMission) {
        return new MissionHistoryItemResponse(
                userMission.getMission().getId(),
                userMission.getMission().getTitle(),
                userMission.getMission().getType(),
                userMission.getDate(),
                userMission.getPerformed(),
                userMission.getPerformed() ? userMission.getPerformedAt() : null
        );
    }
}
