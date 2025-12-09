package com.example.ezback.controller;

import com.example.ezback.dto.question.*;
import com.example.ezback.entity.User;
import com.example.ezback.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



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

    @PostMapping("/answer")
    @Operation(summary = "오늘의 질문 답변 제출", description = "사용자가 오늘의 질문에 대한 답변을 제출합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "답변 제출 성공",
                    content = @Content(schema = @Schema(implementation = SubmitAnswerResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 - 필드 부족 또는 questionId 없음"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 로그인이 필요합니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "질문 없음 - 질문을 찾을 수 없습니다."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복 제출 - 오늘의 질문은 이미 답변을 제출했습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 - 답변 제출 중 오류가 발생했습니다."
            )
    })
    public ResponseEntity<SubmitAnswerResponse> submitAnswer(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SubmitAnswerRequest request
    ) {
        SubmitAnswerResponse response = questionService.submitAnswer(user, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(summary = "지난 질문 목록 조회", description = "사용자가 과거에 받았던 질문 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "질문 히스토리 조회 성공",
                    content = @Content(schema = @Schema(implementation = QuestionHistoryListResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 로그인이 필요합니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "데이터 없음 - 기록된 질문이 없습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 - 지난 질문 목록을 조회할 수 없습니다."
            )
    })
    public ResponseEntity<QuestionHistoryListResponse> getQuestionHistory(
            @AuthenticationPrincipal User user
    ) {
        // TODO: 페이징 파라미터 지원 필요 (명세에서 "고려 가능"이라고 명시, 구체적 파라미터 미지정)
        // @RequestParam(defaultValue = "0") int page,
        // @RequestParam(defaultValue = "20") int size

        QuestionHistoryListResponse response = questionService.getQuestionHistory(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "지난 질문 상세 조회", description = "특정 questionId에 해당하는 과거 질문 1개를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "질문 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = QuestionHistoryResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 로그인이 필요합니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "질문 없음 - 해당 질문을 찾을 수 없습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류 - 질문 상세 정보를 조회할 수 없습니다."
            )
    })
    public ResponseEntity<QuestionHistoryResponse> getQuestionDetail(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionId
    ) {
        QuestionHistoryResponse response = questionService.getQuestionDetail(user, questionId);
        return ResponseEntity.ok(response);
    }
}
