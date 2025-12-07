package com.example.ezback.controller;

import com.example.ezback.dto.anniversary.AnniversariesResponse;
import com.example.ezback.dto.anniversary.AnniversaryResponse;
import com.example.ezback.entity.User;
import com.example.ezback.service.AnniversaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/anniversaries")
@RequiredArgsConstructor
@Tag(name = "Anniversary", description = "기념일 관리 API")
@SecurityRequirement(name = "Bearer Authentication")
public class AnniversaryController {

    private final AnniversaryService anniversaryService;

    @GetMapping
    @Operation(
            summary = "기념일 목록 조회",
            description = "커플의 모든 기념일을 조회합니다. 정렬 옵션을 지정할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "커플 연결 필요"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<AnniversariesResponse> getAnniversaries(
            @AuthenticationPrincipal User user,
            @Parameter(description = "정렬 순서: asc (오름차순, 기본값) 또는 desc (내림차순)")
            @RequestParam(required = false, defaultValue = "asc") String sort
    ) {
        AnniversariesResponse response = anniversaryService.getAnniversaries(user, sort);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paged")
    @Operation(
            summary = "기념일 목록 조회 (페이징)",
            description = "커플의 기념일을 페이징하여 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "커플 연결 필요"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<Page<AnniversaryResponse>> getAnniversariesPaged(
            @AuthenticationPrincipal User user,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(required = false, defaultValue = "10") int size,
            @Parameter(description = "정렬 필드 (기본값: date)")
            @RequestParam(required = false, defaultValue = "date") String sortBy,
            @Parameter(description = "정렬 순서: asc 또는 desc (기본값: asc)")
            @RequestParam(required = false, defaultValue = "asc") String sortOrder
    ) {
        Page<AnniversaryResponse> response = anniversaryService.getAnniversariesWithPagination(
                user, page, size, sortBy, sortOrder
        );
        return ResponseEntity.ok(response);
    }
}
