package com.project.secretdiary.dto.request.member;

import lombok.*;

import javax.validation.constraints.Max;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    private String url;

    @Max(value = 100, message = "100자 이하로 입력하세요.")
    private String statusMessage;

}
