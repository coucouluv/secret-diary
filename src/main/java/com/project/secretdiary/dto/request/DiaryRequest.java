package com.project.secretdiary.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequest {

    @NotBlank(message = "친구 아이디를 입력하세요.")
    private String friendUserId;

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String text;

    private String url;

}
