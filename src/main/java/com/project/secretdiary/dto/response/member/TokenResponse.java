package com.project.secretdiary.dto.response.member;


import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long reExpireTime;

    public TokenResponse(final String accessToken, final String refreshToken,
                         final long reExpireTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.reExpireTime = reExpireTime;
    }
}
