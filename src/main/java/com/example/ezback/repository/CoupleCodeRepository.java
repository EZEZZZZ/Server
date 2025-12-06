package com.example.ezback.repository;

import com.example.ezback.entity.CoupleCode;
import com.example.ezback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoupleCodeRepository extends JpaRepository<CoupleCode, Long> {

    Optional<CoupleCode> findByUser(User user);

    Optional<CoupleCode> findByCode(String code);

    boolean existsByCode(String code);

    void deleteByUser(User user);
}
