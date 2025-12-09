package com.example.ezback.repository;

import com.example.ezback.entity.User;
import com.example.ezback.entity.UserDailyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDailyQuestionRepository extends JpaRepository<UserDailyQuestion, Long> {

    Optional<UserDailyQuestion> findByUserAndDate(User user, LocalDate date);

    // 날짜 기준 내림차순 정렬로 사용자의 질문 히스토리 조회
    List<UserDailyQuestion> findByUserOrderByDateDesc(User user);

    // TODO: 페이징 지원 필요 (명세에서 "고려 가능"이라고 명시, 구체적 파라미터 미지정)
    // Page<UserDailyQuestion> findByUserOrderByDateDesc(User user, Pageable pageable);

    // TODO: 최대 조회 기간 제한 필요 (명세에서 "예: 최근 30일"이라고 명시, 구체적 정책 미정)
    // List<UserDailyQuestion> findByUserAndDateAfterOrderByDateDesc(User user, LocalDate startDate);
}
