package com.example.ezback.repository;

import com.example.ezback.entity.User;
import com.example.ezback.entity.UserMission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    Optional<UserMission> findByUserAndDate(User user, LocalDate date);

    Page<UserMission> findByUserOrderByDateDesc(User user, Pageable pageable);

    // TODO: 날짜 정렬 방향 선택 기능 필요 여부 결정 (명세에서 "최신순 또는 오름차순" 언급, 구체적 파라미터 미명시)
    // TODO: 미션 타입(type) 필터링 기능 필요 여부 결정 (명세에서 "미션 유형 필터 기능 고려" 언급, 구체적 구현 미명시)
}
