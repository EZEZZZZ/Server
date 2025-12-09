package com.example.ezback.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 30)
    private String keyword;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // TODO: 동일 키워드 중복 저장 정책 결정 필요
    // (1) 최신 검색 기준으로 timestamp 업데이트
    // (2) 중복 저장 허용
    // (3) 최근 N개만 보관 (예: 최대 10개)

    // TODO: 검색어 정규화 정책 결정 필요
    // - 대소문자 구분 여부 (현재는 저장 시 소문자로 변환)
    // - 특수문자 처리 방법
}
