package com.example.ezback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "삭제 성공 응답")
public class DeleteResponse {

    @Schema(description = "성공 메시지", example = "기념일이 삭제되었습니다.")
    private String message;
}
