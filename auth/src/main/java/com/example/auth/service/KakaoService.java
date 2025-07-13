package com.example.auth.service;

import com.example.auth.dto.KakaoTokenDto;
import com.example.auth.dto.KakaoUserInfoDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    private String clientId;
    private final String KAUTH_TOKEN_URL_HOST ;
    private final String KAUTH_USER_URL_HOST;

    @Autowired
    public KakaoService(@Value("${kakao.client_id}") String clientId) {
        this.clientId = clientId;
        KAUTH_TOKEN_URL_HOST ="https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    }

    public String getAccessTokenFromKakao(String code) {

        KakaoTokenDto kakaoTokenDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoTokenDto.class)
                .block();


        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenDto.getRefreshToken());
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenDto.getScope());

        return kakaoTokenDto.getAccessToken();
    }

    public KakaoUserInfoDto getUserInfo(String accessToken) {

        KakaoUserInfoDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoUserInfoDto.class)
                .block();

        log.info("[ Kakao Service ] Nickname ---> {} ", userInfo.getNickname());

        return userInfo;
    }


}
