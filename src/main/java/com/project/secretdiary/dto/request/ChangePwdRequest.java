package com.project.secretdiary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePwdRequest {
    @NotBlank(message = "현재 비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "변경할 비밀번호를 입력하세요.")
    private String changePassword;
}
