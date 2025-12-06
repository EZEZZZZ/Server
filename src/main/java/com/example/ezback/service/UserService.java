package com.example.ezback.service;

import com.example.ezback.dto.UserInfoResponse;
import com.example.ezback.entity.Couple;
import com.example.ezback.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Transactional(readOnly = true)
    public UserInfoResponse getCurrentUserInfo(User currentUser) {
        if (!currentUser.isInCouple()) {
            return UserInfoResponse.builder()
                    .userId(currentUser.getId())
                    .name(currentUser.getName())
                    .email(currentUser.getEmail())
                    .profileImage(currentUser.getPicture())
                    .connected(false)
                    .partnerId(null)
                    .build();
        }

        Couple couple = currentUser.getCouple();
        User partner = couple.getUser1().getId().equals(currentUser.getId())
                ? couple.getUser2()
                : couple.getUser1();

        return UserInfoResponse.builder()
                .userId(currentUser.getId())
                .name(currentUser.getName())
                .email(currentUser.getEmail())
                .profileImage(currentUser.getPicture())
                .connected(true)
                .partnerId(partner.getId())
                .build();
    }
}
