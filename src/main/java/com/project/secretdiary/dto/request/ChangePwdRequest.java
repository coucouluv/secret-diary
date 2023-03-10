package com.project.secretdiary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePwdRequest {
    @NotBlank
    private String password;

    @NotBlank
    private String changePassword;
}
