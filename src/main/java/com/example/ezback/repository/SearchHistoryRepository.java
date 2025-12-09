package com.example.ezback.repository;

import com.example.ezback.entity.SearchHistory;
import com.example.ezback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    // TODO: 동일 키워드 중복 저장 정책 구현 필요
    // (1) 최신 검색 기준으로 timestamp 업데이트 - findByUserAndKeyword 사용
    // (2) 중복 저장 허용 - 별도 쿼리 불필요
    // (3) 최근 N개만 보관 - countByUser, deleteOldestByUser 등 추가 필요

    Optional<SearchHistory> findByUserAndKeyword(User user, String keyword);
}
