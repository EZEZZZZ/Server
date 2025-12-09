package com.example.ezback.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "검색 키워드 히스토리 저장 요청")
public class SaveSearchHistoryRequest {

    @NotBlank(message = "검색어(keyword)가 필요합니다.")
    @Size(min = 1, max = 30, message = "검색어는 1자 이상 30자 이하로 입력해주세요.")
    @Schema(description = "검색 키워드", example = "행복", requiredMode = Schema.RequiredMode.REQUIRED)
    private String keyword;
}
