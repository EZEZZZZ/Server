package com.example.ezback.dto.mission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "미션 완료 체크 응답")
public class CompleteMissionResponse {

    @Schema(description = "성공 메시지", example = "미션이 성공적으로 완료되었습니다.")
    private String message;
}
