package com.example.ezback.repository;

import com.example.ezback.entity.User;
import com.example.ezback.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    Optional<UserMission> findByUserAndDate(User user, LocalDate date);
}
