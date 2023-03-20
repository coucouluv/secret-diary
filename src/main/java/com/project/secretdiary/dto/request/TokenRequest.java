package com.project.secretdiary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {
    @NotBlank(message = "access token을 입력하세요.")
    private String accessToken;
    @NotBlank(message = "refresh token을 입력하세요.")
    private String refreshToken;
}