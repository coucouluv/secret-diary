package com.project.secretdiary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자 생성
@NoArgsConstructor //기본 생성자 생성
public class TokenRequest {
    @NotBlank(message = "access token을 입력하세요.")
    private String accessToken;
    @NotBlank(message = "refresh token을 입력하세요.")
    private String refreshToken;
}