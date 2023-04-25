package com.project.secretdiary.dto.response.diary;

import com.project.secretdiary.entity.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DiaryDetailResponse {

    private Long diaryId;

    private String title;

    private String text;

    private String image;
    private LocalDateTime saveDate;

    public DiaryDetailResponse(final Diary diary) {
        this.diaryId = diary.getId();
        this.title = diary.getTitle();
        this.text = diary.getText();
        this.image = diary.getImage();
        this.saveDate = diary.getSaveDate();
    }

}
