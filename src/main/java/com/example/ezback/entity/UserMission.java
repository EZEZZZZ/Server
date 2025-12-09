package com.example.ezback.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_missions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean performed;

    private LocalDateTime performedAt;

    @Column(length = 500)
    private String evidence;

    @Column(length = 500)
    private String imageUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void markAsPerformed(String evidence, String imageUrl) {
        this.performed = true;
        this.performedAt = LocalDateTime.now();
        this.evidence = evidence;
        this.imageUrl = imageUrl;
    }

    // TODO: 이미지 업로드 처리 정책 결정 필요 (명세에서 "S3 업로드 후 URL 저장" 언급, 구체적 구현 미명시)
}
