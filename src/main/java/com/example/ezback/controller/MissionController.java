package com.example.ezback.controller;

import com.example.ezback.dto.mission.CompleteMissionRequest;
import com.example.ezback.dto.mission.CompleteMissionResponse;
import com.example.ezback.dto.mission.MissionHistoryResponse;
import com.example.ezback.dto.mission.TodayMissionResponse;
import com.example.ezback.entity.User;
import com.example.ezback.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/missions")
@RequiredArgsConstructor
@Tag(name = "Mission", description = "미션 관련 API")
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/today")
    @Operation(summary = "오늘의 미션 조회", description = "사용자에게 배정된 오늘의 미션을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "오늘의 미션 조회 성공",
                    content = @Content(schema = @Schema(implementation = TodayMissionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 로그인이 필요합니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "미션 없음 - 오늘의 미션이 존재하지 않습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 - 오늘의 미션을 조회할 수 없습니다."
            )
    })
    public ResponseEntity<TodayMissionResponse> getTodayMission(
            @AuthenticationPrincipal User user
    ) {
        TodayMissionResponse response = missionService.getTodayMission(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete")
    @Operation(summary = "미션 완료 체크", description = "오늘의 미션을 완료 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 완료 성공",
                    content = @Content(schema = @Schema(implementation = CompleteMissionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 - 이미 완료된 미션입니다."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 로그인이 필요합니다."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "충돌 - 오늘 배정된 미션이 없습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 - 미션을 완료할 수 없습니다."
            )
    })
    public ResponseEntity<CompleteMissionResponse> completeMission(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CompleteMissionRequest request
    ) {
        CompleteMissionResponse response = missionService.completeMission(user, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(summary = "미션 히스토리 조회", description = "사용자의 과거 미션 기록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 히스토리 조회 성공",
                    content = @Content(schema = @Schema(implementation = MissionHistoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 로그인이 필요합니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "히스토리 없음 - 조회 가능한 미션 기록이 없습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 - 미션 히스토리를 조회할 수 없습니다."
            )
    })
    public ResponseEntity<MissionHistoryResponse> getMissionHistory(
            @AuthenticationPrincipal User user,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        MissionHistoryResponse response = missionService.getMissionHistory(user, pageable);
        return ResponseEntity.ok(response);
    }
}
