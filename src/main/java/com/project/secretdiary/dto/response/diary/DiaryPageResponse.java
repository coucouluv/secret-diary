package com.project.secretdiary.dto.response.diary;


import lombok.Getter;
import java.util.List;

@Getter
public class DiaryPageResponse {
    private boolean hasNext;
    private Long nextId;
    List<DiaryResponse> diaries;

    public DiaryPageResponse(final boolean hasNext, final Long nextId, final List<DiaryResponse> diaries) {
        this.hasNext = hasNext;
        this.nextId = nextId;
        this.diaries = diaries;
    }

}
