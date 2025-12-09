package com.example.ezback.controller;

import com.example.ezback.dto.question.TodayQuestionResponse;
import com.example.ezback.entity.User;
import com.example.ezback.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Tag(name = "Question", description = "질문 관련 API")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/today")
    @Operation(summary = "오늘의 질문 조회", description = "사용자에게 배정된 오늘의 질문을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "오늘의 질문 조회 성공",
                    content = @Content(schema = @Schema(implementation = TodayQuestionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 로그인이 필요합니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "질문 없음 - 오늘의 질문이 존재하지 않습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 - 오늘의 질문을 조회할 수 없습니다."
            )
    })
    public ResponseEntity<TodayQuestionResponse> getTodayQuestion(
            @AuthenticationPrincipal User user
    ) {
        TodayQuestionResponse response = questionService.getTodayQuestion(user);
        return ResponseEntity.ok(response);
    }
}
