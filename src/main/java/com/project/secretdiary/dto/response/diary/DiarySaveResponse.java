package com.project.secretdiary.dto.response.diary;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiarySaveResponse {
    private Long diaryId;

    public DiarySaveResponse(final Long diaryId) {
        this.diaryId = diaryId;
    }
}
