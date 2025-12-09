package com.example.ezback.repository;

import com.example.ezback.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    // TODO: 미션 배정 방식 구현 필요 (명세에서 구체적 방식 미지정)
    // - 완전 랜덤: ORDER BY RANDOM() LIMIT 1
    // - 순환 방식: 사용자별 마지막 받은 미션 다음 미션 선택
    // - 타입별 순환: ACTION과 QUESTION을 번갈아 배정
    // - 난이도 기반: 사용자 레벨에 맞는 미션 선택

    @Query(value = "SELECT * FROM missions ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Mission> findRandomMission();
}
