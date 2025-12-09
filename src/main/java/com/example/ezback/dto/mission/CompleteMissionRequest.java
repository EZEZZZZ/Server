package com.example.ezback.dto.mission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "미션 완료 체크 요청")
public class CompleteMissionRequest {

    @Size(max = 500, message = "인증 내용은 최대 500자까지 입력 가능합니다.")
    @Schema(description = "인증 내용", example = "오늘 칭찬 인증 완료!", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String evidence;

    @Size(max = 500, message = "이미지 URL은 최대 500자까지 입력 가능합니다.")
    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String imageUrl;

    // TODO: 이미지 URL 형식 검증 필요 여부 결정 (명세에서 URL 형식 검증 언급 없음)
}
