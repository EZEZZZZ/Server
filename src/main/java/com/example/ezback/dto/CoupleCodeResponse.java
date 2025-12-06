package com.example.ezback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CoupleCodeResponse {
    private String code;
    private LocalDateTime expiresAt;
    private String message;
}
