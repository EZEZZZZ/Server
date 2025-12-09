package com.example.ezback.service;

import com.example.ezback.dto.question.SubmitAnswerRequest;
import com.example.ezback.dto.question.SubmitAnswerResponse;
import com.example.ezback.dto.question.TodayQuestionResponse;
import com.example.ezback.entity.Question;
import com.example.ezback.entity.User;
import com.example.ezback.entity.UserDailyQuestion;
import com.example.ezback.exception.AlreadyAnsweredException;
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

    @Transactional
    public SubmitAnswerResponse submitAnswer(User user, SubmitAnswerRequest request) {
        LocalDate today = LocalDate.now();

        // 사용자의 오늘 질문 조회 (사용자 ID + 날짜 기준 중복 제출 체크)
        UserDailyQuestion userDailyQuestion = userDailyQuestionRepository
                .findByUserAndDate(user, today)
                .orElseThrow(() -> new QuestionNotFoundException("질문을 찾을 수 없습니다."));

        // questionId 검증
        if (!userDailyQuestion.getQuestion().getId().equals(request.getQuestionId())) {
            throw new QuestionNotFoundException("질문을 찾을 수 없습니다.");
        }

        // 이미 답변 제출 여부 확인
        if (userDailyQuestion.getAnswered()) {
            throw new AlreadyAnsweredException("오늘의 질문은 이미 답변을 제출했습니다.");
        }

        // 답변 업데이트
        userDailyQuestion.updateAnswer(request.getAnswer());
        userDailyQuestionRepository.save(userDailyQuestion);

        return new SubmitAnswerResponse("답변이 성공적으로 제출되었습니다.");
    }
}
