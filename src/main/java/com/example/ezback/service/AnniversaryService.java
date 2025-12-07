package com.example.ezback.service;

import com.example.ezback.dto.DeleteResponse;
import com.example.ezback.dto.anniversary.AnniversariesResponse;
import com.example.ezback.dto.anniversary.AnniversaryResponse;
import com.example.ezback.dto.anniversary.CreateAnniversaryRequest;
import com.example.ezback.entity.Anniversary;
import com.example.ezback.entity.Couple;
import com.example.ezback.entity.User;
import com.example.ezback.exception.AnniversaryNotFoundException;
import com.example.ezback.exception.CoupleNotFoundException;
import com.example.ezback.exception.DuplicateAnniversaryException;
import com.example.ezback.exception.ForbiddenException;
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

    /**
     * Create a new anniversary for the couple
     *
     * @param user Current authenticated user
     * @param request Anniversary creation request
     * @return Created anniversary response
     */
    @Transactional
    public AnniversaryResponse createAnniversary(User user, CreateAnniversaryRequest request) {
        Couple couple = user.getCouple();

        if (couple == null) {
            throw new CoupleNotFoundException("커플 연결이 필요합니다.");
        }

        // Check for duplicate anniversary (title + date + couple)
        if (anniversaryRepository.existsByCoupleAndTitleAndDate(
                couple,
                request.getTitle(),
                request.getDate())) {
            throw new DuplicateAnniversaryException("해당 기념일이 이미 존재합니다.");
        }

        // Create new anniversary
        Anniversary anniversary = Anniversary.builder()
                .couple(couple)
                .title(request.getTitle())
                .date(request.getDate())
                .repeat(request.getRepeat())
                .memo(request.getMemo())
                .build();

        Anniversary savedAnniversary = anniversaryRepository.save(anniversary);

        return AnniversaryResponse.from(savedAnniversary);
    }

    /**
     * Delete an anniversary
     *
     * @param user Current authenticated user
     * @param anniversaryId Anniversary ID to delete
     * @return Delete success message
     */
    @Transactional
    public DeleteResponse deleteAnniversary(User user, Long anniversaryId) {
        // 1. Find anniversary by ID
        Anniversary anniversary = anniversaryRepository.findById(anniversaryId)
                .orElseThrow(() -> new AnniversaryNotFoundException("기념일을 찾을 수 없습니다."));

        // 2. Get user's couple
        Couple userCouple = user.getCouple();

        if (userCouple == null) {
            throw new CoupleNotFoundException("커플 연결이 필요합니다.");
        }

        // 3. Verify ownership: Check if anniversary belongs to user's couple
        Couple anniversaryCouple = anniversary.getCouple();

        if (!anniversaryCouple.getId().equals(userCouple.getId())) {
            throw new ForbiddenException("해당 기념일을 삭제할 권한이 없습니다.");
        }

        // 4. Delete anniversary (hard delete)
        anniversaryRepository.delete(anniversary);

        return new DeleteResponse("기념일이 삭제되었습니다.");
    }
}
