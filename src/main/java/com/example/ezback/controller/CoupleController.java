package com.example.ezback.controller;

import com.example.ezback.dto.CoupleCodeResponse;
import com.example.ezback.dto.CoupleConnectRequest;
import com.example.ezback.dto.CoupleConnectResponse;
import com.example.ezback.dto.PartnerResponse;
import com.example.ezback.entity.User;
import com.example.ezback.exception.UnauthorizedException;
import com.example.ezback.service.CoupleCodeService;
import com.example.ezback.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couple")
@RequiredArgsConstructor
public class CoupleController {

    private final CoupleCodeService coupleCodeService;
    private final CoupleService coupleService;

    @PostMapping("/code")
    public ResponseEntity<CoupleCodeResponse> generateCoupleCode(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        User user = (User) authentication.getPrincipal();
        CoupleCodeResponse response = coupleCodeService.generateCoupleCode(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/connect")
    public ResponseEntity<CoupleConnectResponse> connectCouple(
            @RequestBody CoupleConnectRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        User user = (User) authentication.getPrincipal();
        CoupleConnectResponse response = coupleService.connectCouple(user, request.getCode());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/partner")
    public ResponseEntity<PartnerResponse> getPartner(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        User user = (User) authentication.getPrincipal();
        PartnerResponse response = coupleService.getPartner(user);

        return ResponseEntity.ok(response);
    }
}
