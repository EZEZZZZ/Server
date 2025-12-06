package com.example.ezback.repository;

import com.example.ezback.entity.UserEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEmotionRepository extends JpaRepository<UserEmotion, Long> {
}
