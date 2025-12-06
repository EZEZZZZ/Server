package com.example.ezback.service;

import com.example.ezback.dto.CoupleCodeResponse;
import com.example.ezback.entity.CoupleCode;
import com.example.ezback.entity.User;
import com.example.ezback.exception.AlreadyCoupleException;
import com.example.ezback.repository.CoupleCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CoupleCodeService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRATION_DAYS = 7;
    private static final SecureRandom random = new SecureRandom();

    private final CoupleCodeRepository coupleCodeRepository;

    @Transactional
    public CoupleCodeResponse generateCoupleCode(User user) {
        if (user.isInCouple()) {
            throw new AlreadyCoupleException("이미 커플이 연결되어 있어 코드 생성이 불가능합니다.");
        }

        coupleCodeRepository.findByUser(user).ifPresent(existingCode -> {
            coupleCodeRepository.delete(existingCode);
        });

        String code = generateUniqueCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(EXPIRATION_DAYS);

        CoupleCode coupleCode = CoupleCode.builder()
                .code(code)
                .user(user)
                .expiresAt(expiresAt)
                .build();

        coupleCodeRepository.save(coupleCode);

        return new CoupleCodeResponse(code, expiresAt, "커플 코드가 생성되었습니다.");
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (coupleCodeRepository.existsByCode(code));
        return code;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
