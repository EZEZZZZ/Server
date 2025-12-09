package com.example.ezback.oauth;

import com.example.ezback.entity.User;
import com.example.ezback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oauth2User.getAttributes();

        String providerId = attributes.get("sub").toString();
        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();
        String picture = attributes.get("picture") != null ? attributes.get("picture").toString() : null;

        log.info("OAuth2 로그인 시도 - provider: {}, email: {}, providerId: {}", provider, email, providerId);

        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    log.info("신규 사용자 자동 회원가입 - provider: {}, email: {}", provider, email);
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .picture(picture)
                            .provider(provider)
                            .providerId(providerId)
                            .build();
                    User savedUser = userRepository.save(newUser);
                    log.info("신규 사용자 생성 완료 - userId: {}, email: {}", savedUser.getId(), savedUser.getEmail());
                    return savedUser;
                });

        // 기존 사용자의 경우 프로필 정보 업데이트
        if (!user.getName().equals(name) ||
            (picture != null && !picture.equals(user.getPicture()))) {
            log.info("사용자 프로필 업데이트 - userId: {}, email: {}", user.getId(), user.getEmail());
            user.updateProfile(name, picture);
            userRepository.save(user);
        }

        log.info("OAuth2 로그인 성공 - userId: {}, email: {}", user.getId(), user.getEmail());
        return new CustomOAuth2User(oauth2User, user.getId());
    }
}