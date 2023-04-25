package com.project.secretdiary.dto.request.diary;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryUpdateRequest {

    @NotNull(message = "다이어리 아이디를 입력하세요.")
    private Long diaryId;

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String text;

    private String image;


}