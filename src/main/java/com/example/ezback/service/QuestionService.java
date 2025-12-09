package com.example.ezback.service;

import com.example.ezback.dto.question.*;
import com.example.ezback.entity.Question;
import com.example.ezback.entity.SearchHistory;
import com.example.ezback.entity.User;
import com.example.ezback.entity.UserDailyQuestion;
import com.example.ezback.exception.*;
import com.example.ezback.repository.QuestionRepository;
import com.example.ezback.repository.SearchHistoryRepository;
import com.example.ezback.repository.UserDailyQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserDailyQuestionRepository userDailyQuestionRepository;
    private final SearchHistoryRepository searchHistoryRepository;

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

    @Transactional(readOnly = true)
    public QuestionHistoryListResponse getQuestionHistory(User user) {
        // 사용자의 질문 히스토리 조회 (날짜 기준 내림차순 정렬)
        List<UserDailyQuestion> historyList = userDailyQuestionRepository
                .findByUserOrderByDateDesc(user);

        // TODO: 페이징 처리 필요 (명세에서 "고려 가능"이라고 명시, 구체적 파라미터 미지정)
        // TODO: 최대 조회 기간 제한 필요 (명세에서 "예: 최근 30일"이라고 명시, 구체적 정책 미정)

        // 히스토리가 비어있으면 404 예외 발생
        if (historyList.isEmpty()) {
            throw new QuestionHistoryNotFoundException("기록된 질문이 없습니다.");
        }

        List<QuestionHistoryResponse> historyResponses = historyList.stream()
                .map(QuestionHistoryResponse::from)
                .collect(Collectors.toList());

        return new QuestionHistoryListResponse(historyResponses);
    }

    @Transactional(readOnly = true)
    public QuestionHistoryResponse getQuestionDetail(User user, Long userDailyQuestionId) {
        // UserDailyQuestion 조회
        UserDailyQuestion userDailyQuestion = userDailyQuestionRepository
                .findById(userDailyQuestionId)
                .orElseThrow(() -> new QuestionNotFoundException("해당 질문을 찾을 수 없습니다."));

        // 사용자 본인의 질문인지 권한 체크
        if (!userDailyQuestion.getUser().getId().equals(user.getId())) {
            // TODO: 권한 없을 때 403 Forbidden vs 404 Not Found 정책 결정 필요
            // 현재는 리소스 존재 여부를 숨기기 위해 404 반환 (보안상 권장)
            throw new QuestionNotFoundException("해당 질문을 찾을 수 없습니다.");
        }

        return QuestionHistoryResponse.from(userDailyQuestion);
    }

    @Transactional(readOnly = true)
    public QuestionSearchResultResponse searchQuestions(String keyword, int page, int size) {
        // keyword 검증
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new SearchKeywordRequiredException("검색어(keyword)가 필요합니다.");
        }

        // TODO: keyword 최소 길이 정책 결정 필요 (명세에서 "최소 1글자 이상" 언급)
        // 현재는 공백 제거 후 빈 문자열만 체크
        if (keyword.trim().length() < 1) {
            throw new SearchKeywordRequiredException("검색어(keyword)가 필요합니다.");
        }

        // 페이징 처리하여 검색
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionPage = questionRepository.searchByKeyword(keyword.trim(), pageable);

        // 검색 결과가 없으면 404 예외
        if (questionPage.isEmpty()) {
            throw new QuestionSearchNotFoundException("검색 결과가 없습니다.");
        }

        // DTO 변환
        List<QuestionSearchItemResponse> results = questionPage.getContent().stream()
                .map(QuestionSearchItemResponse::from)
                .collect(Collectors.toList());

        return QuestionSearchResultResponse.from(questionPage, results);
    }

    @Transactional
    public SearchHistoryResponse saveSearchHistory(User user, SaveSearchHistoryRequest request) {
        // keyword 검증 및 정규화
        String keyword = request.getKeyword();
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new SearchKeywordRequiredException("검색어(keyword)가 필요합니다.");
        }

        // 검색어 정규화: trim 처리
        String normalizedKeyword = keyword.trim();

        // TODO: 검색어 정규화 정책 결정 필요 (명세에서 "trim, lower-case 등" 언급, 구체적 정책 미정)
        // 현재는 trim만 적용, 대소문자 변환 여부는 정책 결정 필요
        // normalizedKeyword = normalizedKeyword.toLowerCase();

        // TODO: 동일 키워드 중복 저장 정책 결정 필요
        // (1) 최신 검색 기준으로 timestamp 업데이트 - 기존 레코드 삭제 후 재생성
        // (2) 중복 저장 허용 - 현재 구현 방식
        // (3) 최근 N개만 보관 - 개수 체크 후 오래된 것 삭제
        // 현재는 중복 저장 허용 방식으로 구현

        SearchHistory searchHistory = SearchHistory.builder()
                .user(user)
                .keyword(normalizedKeyword)
                .build();

        SearchHistory savedHistory = searchHistoryRepository.save(searchHistory);

        return SearchHistoryResponse.from(savedHistory);
    }
}
