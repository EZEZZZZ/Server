package com.example.ezback.service;

import com.example.ezback.dto.mission.CompleteMissionRequest;
import com.example.ezback.dto.mission.CompleteMissionResponse;
import com.example.ezback.dto.mission.MissionHistoryItemResponse;
import com.example.ezback.dto.mission.MissionHistoryResponse;
import com.example.ezback.dto.mission.TodayMissionResponse;
import com.example.ezback.entity.Mission;
import com.example.ezback.entity.User;
import com.example.ezback.entity.UserMission;
import com.example.ezback.exception.AlreadyCompletedException;
import com.example.ezback.exception.MissionHistoryNotFoundException;
import com.example.ezback.exception.MissionNotFoundException;
import com.example.ezback.exception.MissionNotTodayException;
import com.example.ezback.repository.MissionRepository;
import com.example.ezback.repository.UserMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;

    @Transactional
    public TodayMissionResponse getTodayMission(User user) {
        LocalDate today = LocalDate.now();

        // 오늘 날짜로 이미 배정된 미션이 있는지 확인
        UserMission userMission = userMissionRepository
                .findByUserAndDate(user, today)
                .orElseGet(() -> assignTodayMission(user, today));

        return TodayMissionResponse.from(userMission);
    }

    private UserMission assignTodayMission(User user, LocalDate date) {
        // TODO: 미션 배정 방식 구현 필요 (명세에서 구체적 방식 미지정)
        // 현재는 완전 랜덤 방식으로 구현됨
        // 향후 고려사항:
        // - 타입별 순환 (ACTION과 QUESTION 번갈아 배정)
        // - 사용자별 진행 상황 기반 선택
        // - 난이도 또는 카테고리 기반 추천

        Mission mission = missionRepository.findRandomMission()
                .orElseThrow(() -> new MissionNotFoundException("오늘의 미션이 존재하지 않습니다."));

        UserMission userMission = UserMission.builder()
                .user(user)
                .mission(mission)
                .date(date)
                .performed(false)
                .build();

        return userMissionRepository.save(userMission);
    }

    @Transactional
    public CompleteMissionResponse completeMission(User user, CompleteMissionRequest request) {
        LocalDate today = LocalDate.now();

        // 오늘 배정된 미션 조회
        UserMission userMission = userMissionRepository
                .findByUserAndDate(user, today)
                .orElseThrow(() -> new MissionNotTodayException("오늘 배정된 미션이 없습니다."));

        // 이미 완료된 미션인지 확인
        if (userMission.getPerformed()) {
            throw new AlreadyCompletedException("이미 완료된 미션입니다.");
        }

        // 미션 완료 처리
        userMission.markAsPerformed(request.getEvidence(), request.getImageUrl());

        return new CompleteMissionResponse("미션이 성공적으로 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public MissionHistoryResponse getMissionHistory(User user, Pageable pageable) {
        Page<UserMission> missionPage = userMissionRepository.findByUserOrderByDateDesc(user, pageable);

        // 히스토리가 없는 경우 예외 발생
        if (missionPage.isEmpty()) {
            throw new MissionHistoryNotFoundException("조회 가능한 미션 기록이 없습니다.");
        }

        List<MissionHistoryItemResponse> missions = missionPage.getContent().stream()
                .map(MissionHistoryItemResponse::from)
                .collect(Collectors.toList());

        return MissionHistoryResponse.from(missionPage, missions);

        // TODO: 커플 단위 미션 기록 조회 여부 결정 필요 (명세에서 "커플 단위 미션 기록인지 여부 정책 필요" 언급)
        // TODO: 하루 미션 개수 정책 결정 필요 (명세에서 "하루에 하나의 미션일 수도, 여러 개일 수도" 언급)
    }
}
