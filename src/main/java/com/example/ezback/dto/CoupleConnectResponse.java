package com.example.ezback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoupleConnectResponse {
    private Long partnerId;
    private String partnerName;
    private String message;
}
