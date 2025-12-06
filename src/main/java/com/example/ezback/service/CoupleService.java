package com.example.ezback.service;

import com.example.ezback.dto.CoupleConnectResponse;
import com.example.ezback.dto.CoupleStatusResponse;
import com.example.ezback.dto.PartnerResponse;
import com.example.ezback.entity.Couple;
import com.example.ezback.entity.CoupleCode;
import com.example.ezback.entity.User;
import com.example.ezback.exception.AlreadyCoupleException;
import com.example.ezback.exception.CoupleCodeNotFoundException;
import com.example.ezback.exception.InvalidRequestException;
import com.example.ezback.exception.NoCoupleException;
import com.example.ezback.repository.CoupleCodeRepository;
import com.example.ezback.repository.CoupleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CoupleService {

    private final CoupleCodeRepository coupleCodeRepository;
    private final CoupleRepository coupleRepository;

    @Transactional
    public CoupleConnectResponse connectCouple(User currentUser, String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new InvalidRequestException("커플 코드가 필요합니다.");
        }

        CoupleCode coupleCode = coupleCodeRepository.findByCode(code)
                .orElseThrow(() -> new CoupleCodeNotFoundException("유효하지 않은 커플 코드입니다."));

        if (coupleCode.isExpired()) {
            coupleCodeRepository.delete(coupleCode);
            throw new CoupleCodeNotFoundException("유효하지 않은 커플 코드입니다.");
        }

        User codeOwner = coupleCode.getUser();

        if (codeOwner.getId().equals(currentUser.getId())) {
            throw new InvalidRequestException("자신의 코드로는 연결할 수 없습니다.");
        }

        if (currentUser.isInCouple()) {
            throw new AlreadyCoupleException("이미 커플 상태이므로 연결할 수 없습니다.");
        }

        if (codeOwner.isInCouple()) {
            throw new AlreadyCoupleException("이미 커플 상태이므로 연결할 수 없습니다.");
        }

        Couple couple = Couple.builder()
                .user1(codeOwner)
                .user2(currentUser)
                .build();

        coupleRepository.save(couple);
        coupleCodeRepository.delete(coupleCode);

        return new CoupleConnectResponse(
                codeOwner.getId(),
                codeOwner.getName(),
                "커플이 성공적으로 연결되었습니다."
        );
    }

    @Transactional(readOnly = true)
    public PartnerResponse getPartner(User currentUser) {
        if (!currentUser.isInCouple()) {
            throw new NoCoupleException("아직 커플이 연결되지 않았습니다.");
        }

        Couple couple = currentUser.getCouple();
        User partner = couple.getUser1().getId().equals(currentUser.getId())
                ? couple.getUser2()
                : couple.getUser1();

        return new PartnerResponse(
                partner.getId(),
                partner.getName(),
                partner.getEmail(),
                couple.getConnectedAt(),
                "상대방 정보 조회 성공"
        );
    }

    @Transactional(readOnly = true)
    public CoupleStatusResponse getCoupleStatus(User currentUser) {
        if (!currentUser.isInCouple()) {
            return new CoupleStatusResponse(false);
        }

        Couple couple = currentUser.getCouple();
        User partner = couple.getUser1().getId().equals(currentUser.getId())
                ? couple.getUser2()
                : couple.getUser1();

        return new CoupleStatusResponse(true, partner.getId());
    }
}
