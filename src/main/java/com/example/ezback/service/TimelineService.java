package com.example.ezback.service;

import com.example.ezback.dto.timeline.*;
import com.example.ezback.entity.Anniversary;
import com.example.ezback.entity.Couple;
import com.example.ezback.entity.User;
import com.example.ezback.entity.UserEmotion;
import com.example.ezback.exception.CoupleNotFoundException;
import com.example.ezback.repository.AnniversaryRepository;
import com.example.ezback.repository.UserEmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineService {

    private final AnniversaryRepository anniversaryRepository;
    private final UserEmotionRepository userEmotionRepository;

    /**
     * Get timeline events for the user's couple
     *
     * @param user      Current authenticated user
     * @param sortOrder Sort order: "asc" or "desc" (default: "desc")
     * @return Timeline response with all events
     */
    public TimelineResponse getTimeline(User user, String sortOrder) {
        Couple couple = user.getCouple();

        if (couple == null) {
            throw new CoupleNotFoundException("커플 연결이 필요합니다.");
        }

        List<TimelineEvent> events = new ArrayList<>();

        // 1. Add couple connected event
        events.add(createCoupleConnectedEvent(couple));

        // 2. Add anniversary events
        events.addAll(getAnniversaryEvents(couple));

        // 3. Add emotion events (for both users in the couple)
        events.addAll(getEmotionEvents(couple));

        // 4. Add question_answer events (placeholder - to be implemented later)
        // events.addAll(getQuestionAnswerEvents(couple));

        // 5. Sort events by date
        List<TimelineEvent> sortedEvents = sortEvents(events, sortOrder);

        return TimelineResponse.from(sortedEvents);
    }

    /**
     * Create couple connected event
     */
    private CoupleConnectedEvent createCoupleConnectedEvent(Couple couple) {
        return CoupleConnectedEvent.builder()
                .date(couple.getConnectedAt().toLocalDate())
                .description("커플이 연결되었습니다.")
                .build();
    }

    /**
     * Get all anniversary events for the couple
     */
    private List<TimelineEvent> getAnniversaryEvents(Couple couple) {
        List<Anniversary> anniversaries = anniversaryRepository.findByCoupleOrderByDateAsc(couple);

        return anniversaries.stream()
                .map(anniversary -> AnniversaryEvent.builder()
                        .date(anniversary.getDate())
                        .title(anniversary.getTitle())
                        .memo(anniversary.getMemo())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get all emotion events for both users in the couple
     */
    private List<TimelineEvent> getEmotionEvents(Couple couple) {
        List<UserEmotion> emotions = new ArrayList<>();

        // Get emotions for user1
        emotions.addAll(userEmotionRepository.findByUserOrderByCreatedAtDesc(couple.getUser1()));

        // Get emotions for user2
        emotions.addAll(userEmotionRepository.findByUserOrderByCreatedAtDesc(couple.getUser2()));

        return emotions.stream()
                .map(emotion -> EmotionEvent.builder()
                        .date(emotion.getCreatedAt().toLocalDate())
                        .emotion(emotion.getEmotion())
                        .intensity(emotion.getIntensity())
                        .memo(emotion.getMemo())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Sort timeline events by date
     *
     * @param events    List of timeline events
     * @param sortOrder "asc" for oldest first, "desc" for newest first
     * @return Sorted list of events
     */
    private List<TimelineEvent> sortEvents(List<TimelineEvent> events, String sortOrder) {
        Comparator<TimelineEvent> comparator = Comparator.comparing(TimelineEvent::getDate);

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return events.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
