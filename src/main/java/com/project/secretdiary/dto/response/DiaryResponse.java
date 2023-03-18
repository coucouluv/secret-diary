package com.project.secretdiary.dto.response;

import com.project.secretdiary.entity.DiaryEntity;
import lombok.*;

@Getter
@NoArgsConstructor
public class DiaryResponse {

    private Long diaryId;

    private String title;

    private String url;

    public DiaryResponse(final Long diaryId, final String title, final String url) {
        this.diaryId = diaryId;
        this.title = title;
        this.url = url;
    }

    public void changeUrl(final String preSignedUrl) {
        this.url = preSignedUrl;
    }
}
