package com.example.auth.controller;

import com.example.auth.dto.KakaoUserInfoDto;
import com.example.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoController {

    private final KakaoService kakaoService;

    // 카카오 로그인 성공 후 사용자 정보 확인
    @GetMapping("/user/info")
    public String userInfo(@AuthenticationPrincipal OAuth2User oAuth2User) {
        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String nickname = (String) properties.get("nickname");

        return "login: " + nickname;
    }

    // 로그인 여부 확인용 엔드포인트
    @GetMapping("/login-check")
    public String checkLogin() {
        return "login success";
    }

    @GetMapping("/")
    public String home() {
        return "홈화면";
    }
}
