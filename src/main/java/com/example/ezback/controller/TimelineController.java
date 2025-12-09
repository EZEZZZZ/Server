package com.example.ezback.controller;

import com.example.ezback.dto.ErrorResponse;
import com.example.ezback.dto.timeline.TimelineResponse;
import com.example.ezback.entity.User;
import com.example.ezback.service.TimelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timeline")
@RequiredArgsConstructor
@Tag(name = "Timeline", description = "관계 타임라인 API")
@SecurityRequirement(name = "Bearer Authentication")
public class TimelineController {

    private final TimelineService timelineService;

    @GetMapping
    @Operation(
            summary = "관계 타임라인 조회",
            description = "커플의 모든 이벤트(연결일, 기념일, 감정 기록 등)를 시간순으로 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = TimelineResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"timeline\": [{\"type\": \"couple_connected\", \"date\": \"2025-01-10\", \"description\": \"커플이 연결되었습니다.\"}, {\"type\": \"anniversary\", \"date\": \"2025-02-14\", \"title\": \"100일\", \"memo\": \"만난 지 100일 기념\"}]}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"message\": \"로그인이 필요합니다.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "커플 연결 필요",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"message\": \"커플 연결이 필요합니다.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"message\": \"서버 오류가 발생했습니다.\"}")
                    )
            )
    })
    public ResponseEntity<TimelineResponse> getTimeline(
            @AuthenticationPrincipal User user,
            @Parameter(description = "정렬 순서: asc (과거→현재) 또는 desc (현재→과거, 기본값)")
            @RequestParam(required = false, defaultValue = "desc") String sort
    ) {
        TimelineResponse response = timelineService.getTimeline(user, sort);
        return ResponseEntity.ok(response);
    }
}
