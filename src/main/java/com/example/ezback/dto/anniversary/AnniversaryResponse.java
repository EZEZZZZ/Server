package com.example.ezback.dto.anniversary;

import com.example.ezback.entity.Anniversary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnniversaryResponse {

    private Long anniversaryId;
    private String title;
    private LocalDate date;
    private Boolean repeat;
    private String memo;

    public static AnniversaryResponse from(Anniversary anniversary) {
        return AnniversaryResponse.builder()
                .anniversaryId(anniversary.getId())
                .title(anniversary.getTitle())
                .date(anniversary.getDate())
                .repeat(anniversary.getRepeat())
                .memo(anniversary.getMemo())
                .build();
    }
}
