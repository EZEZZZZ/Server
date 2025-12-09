package com.example.ezback.repository;

import com.example.ezback.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // TODO: 검색 대상 컬럼 확장 필요 (명세에서 "question, category, tags 등" 언급, 정책 미정)
    // 현재는 question 필드만 검색, category와 tags 필드가 엔티티에 추가되면 검색 조건 확장 필요
    // TODO: 대소문자 무시 여부 정책 결정 필요 (현재는 대소문자 무시로 구현)
    // TODO: 정렬 방식 정책 결정 필요 (현재는 생성일 기준 내림차순, 명세에서 "최신순 or 사용량순" 언급)
    @Query("SELECT q FROM Question q WHERE LOWER(q.question) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY q.createdAt DESC")
    Page<Question> searchByKeyword(String keyword, Pageable pageable);
}
