package com.example.ezback.repository;

import com.example.ezback.entity.Anniversary;
import com.example.ezback.entity.Couple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnniversaryRepository extends JpaRepository<Anniversary, Long> {

    /**
     * Find all anniversaries by couple, ordered by date ascending
     */
    List<Anniversary> findByCoupleOrderByDateAsc(Couple couple);

    /**
     * Find all anniversaries by couple, ordered by date descending
     */
    List<Anniversary> findByCoupleOrderByDateDesc(Couple couple);

    /**
     * Find all anniversaries by couple with pagination support
     */
    Page<Anniversary> findByCouple(Couple couple, Pageable pageable);

    /**
     * Count anniversaries by couple
     */
    long countByCouple(Couple couple);

    /**
     * Check if couple has any anniversaries
     */
    boolean existsByCouple(Couple couple);

    /**
     * Check if anniversary with same title and date exists for the couple
     * Used for duplicate prevention
     */
    boolean existsByCoupleAndTitleAndDate(Couple couple, String title, LocalDate date);
}
