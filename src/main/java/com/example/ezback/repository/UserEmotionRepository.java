package com.example.ezback.repository;

import com.example.ezback.entity.User;
import com.example.ezback.entity.UserEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserEmotionRepository extends JpaRepository<UserEmotion, Long> {

    @Query("SELECT ue FROM UserEmotion ue WHERE ue.user = :user " +
           "AND ue.createdAt >= :startOfDay AND ue.createdAt < :endOfDay " +
           "ORDER BY ue.createdAt DESC LIMIT 1")
    Optional<UserEmotion> findTodayEmotionByUser(
            @Param("user") User user,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
