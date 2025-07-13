package com.example.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {

    private Properties properties;

    @Getter
    @NoArgsConstructor
    public static class Properties {
        private String nickname;
    }

    public String getNickname() {
        return properties != null ? properties.nickname : null;
    }
}
