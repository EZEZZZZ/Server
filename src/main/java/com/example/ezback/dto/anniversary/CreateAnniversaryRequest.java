package com.example.ezback.dto.anniversary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnniversaryRequest {

    @NotBlank(message = "잘못된 요청입니다.")
    @Size(max = 100, message = "잘못된 요청입니다.")
    private String title;

    @NotNull(message = "잘못된 요청입니다.")
    private LocalDate date;

    @NotNull(message = "잘못된 요청입니다.")
    private Boolean repeat;

    @Size(max = 500, message = "잘못된 요청입니다.")
    private String memo;
}
