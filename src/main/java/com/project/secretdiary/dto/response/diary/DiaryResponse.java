package com.project.secretdiary.dto.response.diary;

import lombok.*;

@Getter
@NoArgsConstructor
public class DiaryResponse {

    private Long diaryId;

    private String title;

    private String image;

    public DiaryResponse(final Long diaryId, final String title, final String image) {
        this.diaryId = diaryId;
        this.title = title;
        this.image = image;
    }

}
