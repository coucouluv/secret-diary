package com.project.secretdiary.dto.request.member;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    private String image;

    @Length(max = 100, message = "100자 이하로 입력하세요.")
    private String statusMessage;

}
