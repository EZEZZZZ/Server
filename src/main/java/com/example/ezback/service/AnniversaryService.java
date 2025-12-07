package com.example.ezback.service;

import com.example.ezback.dto.anniversary.AnniversariesResponse;
import com.example.ezback.dto.anniversary.AnniversaryResponse;
import com.example.ezback.entity.Anniversary;
import com.example.ezback.entity.Couple;
import com.example.ezback.entity.User;
import com.example.ezback.exception.AnniversaryNotFoundException;
import com.example.ezback.exception.CoupleNotFoundException;
import com.example.ezback.repository.AnniversaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnniversaryService {

    private final AnniversaryRepository anniversaryRepository;

    /**
     * Get all anniversaries for the user's couple
     *
     * @param user Current authenticated user
     * @param sortOrder Sort order: "asc" (default) or "desc"
     * @return List of anniversaries
     */
    public AnniversariesResponse getAnniversaries(User user, String sortOrder) {
        Couple couple = user.getCouple();

        if (couple == null) {
            throw new CoupleNotFoundException("커플 연결이 필요합니다.");
        }

        List<Anniversary> anniversaries;

        if ("desc".equalsIgnoreCase(sortOrder)) {
            anniversaries = anniversaryRepository.findByCoupleOrderByDateDesc(couple);
        } else {
            // Default to ascending order
            anniversaries = anniversaryRepository.findByCoupleOrderByDateAsc(couple);
        }

        List<AnniversaryResponse> anniversaryResponses = anniversaries.stream()
                .map(AnniversaryResponse::from)
                .collect(Collectors.toList());

        return AnniversariesResponse.from(anniversaryResponses);
    }

    /**
     * Get anniversaries with pagination support
     *
     * @param user Current authenticated user
     * @param page Page number (0-indexed)
     * @param size Page size
     * @param sortBy Sort field (default: "date")
     * @param sortOrder Sort order: "asc" or "desc"
     * @return Paginated anniversaries
     */
    public Page<AnniversaryResponse> getAnniversariesWithPagination(
            User user,
            int page,
            int size,
            String sortBy,
            String sortOrder) {

        Couple couple = user.getCouple();

        if (couple == null) {
            throw new CoupleNotFoundException("커플 연결이 필요합니다.");
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        String sortField = (sortBy != null && !sortBy.isEmpty()) ? sortBy : "date";

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Anniversary> anniversaryPage = anniversaryRepository.findByCouple(couple, pageable);

        return anniversaryPage.map(AnniversaryResponse::from);
    }

    /**
     * Check if couple has any anniversaries
     *
     * @param user Current authenticated user
     * @return true if anniversaries exist
     */
    public boolean hasAnniversaries(User user) {
        Couple couple = user.getCouple();

        if (couple == null) {
            return false;
        }

        return anniversaryRepository.existsByCouple(couple);
    }
}
