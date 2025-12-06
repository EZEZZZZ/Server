package com.example.ezback.repository;

import com.example.ezback.entity.Couple;
import com.example.ezback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {

    Optional<Couple> findByUser1OrUser2(User user1, User user2);
}
