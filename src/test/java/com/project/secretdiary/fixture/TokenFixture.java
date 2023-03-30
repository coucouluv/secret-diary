package com.project.secretdiary.fixture;

import com.project.secretdiary.dto.response.member.TokenResponse;

public class TokenFixture {

    public static String 액세스_토큰 = "access";
    public static String 리프레시_토큰 = "access";
    public static long 유효_기간 = 1000;

    public static TokenResponse 토큰_응답() {
        return new TokenResponse(액세스_토큰, 리프레시_토큰, 유효_기간);
    }
}
