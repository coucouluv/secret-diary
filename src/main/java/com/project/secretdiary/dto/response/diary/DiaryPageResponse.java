package com.project.secretdiary.dto.response.diary;


import lombok.Getter;
import java.util.List;

@Getter
public class DiaryPageResponse {
    private boolean hasNext;
    List<DiaryResponse> diaries;

    public DiaryPageResponse(final boolean hasNext, final List<DiaryResponse> diaries) {
        this.hasNext = hasNext;
        this.diaries = diaries;
    }

}
