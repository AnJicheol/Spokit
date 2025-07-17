package com.example.auth.service;

import com.example.auth.dto.KakaoUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class KakaoService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        // 1. Spring Security가 access token 자동 발급
        String accessToken = userRequest.getAccessToken().getTokenValue();
        log.info("[OAuth2UserService] Access Token -> {}", accessToken);

        // 2. 사용자 정보 요청
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 3. attributes에서 Kakao 사용자 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오의 사용자 정보 구조
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) properties.get("nickname");

        log.info("[OAuth2UserService] Kakao nickname -> {}", nickname);

        KakaoUserInfoDto userInfoDto = new KakaoUserInfoDto();
        log.info("[OAuth2UserService] KakaoUserInfoDto.nickname -> {}", userInfoDto.getNickname());

        // 4. Spring Security 인증 세션에 저장할 유저 객체 리턴
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id" // attributes에서 식별자로 쓸 키
        );
    }
}
