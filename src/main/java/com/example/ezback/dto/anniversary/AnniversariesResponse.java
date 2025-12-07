package com.example.ezback.dto.anniversary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnniversariesResponse {

    private List<AnniversaryResponse> anniversaries;

    public static AnniversariesResponse from(List<AnniversaryResponse> anniversaries) {
        return AnniversariesResponse.builder()
                .anniversaries(anniversaries)
                .build();
    }
}
