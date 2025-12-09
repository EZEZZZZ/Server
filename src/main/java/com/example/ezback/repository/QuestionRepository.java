package com.example.ezback.repository;

import com.example.ezback.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // TODO: 질문 로테이션 방식 구현 필요 (명세에서 구체적 방식 미지정)
    // - 완전 랜덤: ORDER BY RANDOM() LIMIT 1
    // - 최근 질문 제외: 사용자의 최근 N개 질문 제외 후 랜덤 선택
    // - 카테고리 기반 추천: 카테고리별 순환 또는 선호도 기반 선택

    @Query(value = "SELECT * FROM questions ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Question> findRandomQuestion();
}
