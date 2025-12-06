package com.example.ezback.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoupleStatusResponse {
    private boolean connected;
    private Long partnerId;

    public CoupleStatusResponse(boolean connected) {
        this.connected = connected;
        this.partnerId = null;
    }
}
