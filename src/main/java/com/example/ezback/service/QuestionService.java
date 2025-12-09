package com.example.ezback.service;

import com.example.ezback.dto.question.TodayQuestionResponse;
import com.example.ezback.entity.Question;
import com.example.ezback.entity.User;
import com.example.ezback.entity.UserDailyQuestion;
import com.example.ezback.exception.QuestionNotFoundException;
import com.example.ezback.repository.QuestionRepository;
import com.example.ezback.repository.UserDailyQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserDailyQuestionRepository userDailyQuestionRepository;

    @Transactional
    public TodayQuestionResponse getTodayQuestion(User user) {
        LocalDate today = LocalDate.now();

        // 오늘 날짜로 이미 배정된 질문이 있는지 확인
        UserDailyQuestion userDailyQuestion = userDailyQuestionRepository
                .findByUserAndDate(user, today)
                .orElseGet(() -> assignTodayQuestion(user, today));

        return TodayQuestionResponse.from(userDailyQuestion);
    }

    private UserDailyQuestion assignTodayQuestion(User user, LocalDate date) {
        // TODO: 질문 로테이션 방식 구현 필요 (명세에서 구체적 방식 미지정)
        // 현재는 완전 랜덤 방식으로 구현됨
        // 향후 고려사항:
        // - 최근 N일간 받았던 질문 제외
        // - 사용자 선호 카테고리 기반 추천
        // - 질문 난이도 순환

        Question question = questionRepository.findRandomQuestion()
                .orElseThrow(() -> new QuestionNotFoundException("오늘의 질문이 존재하지 않습니다."));

        UserDailyQuestion userDailyQuestion = UserDailyQuestion.builder()
                .user(user)
                .question(question)
                .date(date)
                .answered(false)
                .build();

        return userDailyQuestionRepository.save(userDailyQuestion);
    }
}
