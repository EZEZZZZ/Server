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
@Schema(description = "오늘의 미션 응답")
public class TodayMissionResponse {

    @Schema(description = "미션 ID", example = "42")
    private Long missionId;

    @Schema(description = "미션 제목", example = "오늘 서로에게 칭찬 1개씩 하기")
    private String title;

    @Schema(description = "미션 타입", example = "ACTION")
    private MissionType type;

    @Schema(description = "미션 배정 날짜", example = "2025-02-01")
    private LocalDate date;

    @Schema(description = "미션 수행 여부", example = "false")
    private Boolean performed;

    @Schema(description = "미션 수행 시각", example = "2025-02-01T18:20:00")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime performedAt;

    public static TodayMissionResponse from(UserMission userMission) {
        return new TodayMissionResponse(
                userMission.getMission().getId(),
                userMission.getMission().getTitle(),
                userMission.getMission().getType(),
                userMission.getDate(),
                userMission.getPerformed(),
                userMission.getPerformed() ? userMission.getPerformedAt() : null
        );
    }

    // TODO: 미션이 없을 때 응답 형식 결정 필요
    // 명세에서 "오늘의 미션 자체가 없는 경우: { "mission": null }" 언급
    // 하지만 다른 응답과 구조가 달라서 일관성 문제
    // (1) 404 예외 발생
    // (2) null 반환
    // (3) Optional wrapper DTO 사용
}
