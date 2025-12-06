package com.example.ezback.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
    private Long userId;
    private String name;
    private String email;
    private String profileImage;
    private boolean connected;
    private Long partnerId;
}
