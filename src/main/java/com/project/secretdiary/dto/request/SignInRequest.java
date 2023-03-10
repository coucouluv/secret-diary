package com.project.secretdiary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor //기본 생성자 생성
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자 생성
public class SignInRequest {
    @NotBlank(message = "아이디를 입력하세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
}
