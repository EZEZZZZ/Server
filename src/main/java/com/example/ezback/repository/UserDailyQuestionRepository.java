package com.example.ezback.repository;

import com.example.ezback.entity.User;
import com.example.ezback.entity.UserDailyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserDailyQuestionRepository extends JpaRepository<UserDailyQuestion, Long> {

    Optional<UserDailyQuestion> findByUserAndDate(User user, LocalDate date);
}
