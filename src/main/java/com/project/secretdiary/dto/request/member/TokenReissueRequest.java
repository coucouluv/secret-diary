package com.project.secretdiary.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenReissueRequest {
    @NotBlank(message = "refresh token을 입력하세요.")
    private String refreshToken;
}
