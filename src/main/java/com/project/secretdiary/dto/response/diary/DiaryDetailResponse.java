package com.project.secretdiary.dto.response.diary;

import com.project.secretdiary.entity.DiaryEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DiaryDetailResponse {

    private Long diaryId;

    private String title;

    private String text;

    private String url;
    private LocalDateTime saveDate;

    public DiaryDetailResponse(final DiaryEntity diary) {
        this.diaryId = diary.getId();
        this.title = diary.getTitle();
        this.text = diary.getText();
        this.url = diary.getUrl();
        this.saveDate = diary.getSaveDate();
    }

}
