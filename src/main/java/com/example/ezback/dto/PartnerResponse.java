package com.example.ezback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PartnerResponse {
    private Long partnerId;
    private String partnerName;
    private String partnerEmail;
    private LocalDateTime connectedAt;
    private String message;
}
