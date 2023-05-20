package com.project.secretdiary.dto.response.diary;

import com.project.secretdiary.dto.response.member.WriterResponse;
import com.project.secretdiary.entity.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DiaryDetailResponse {

    private Long id;
    private String title;

    private String text;

    private String image;
    private LocalDateTime saveDate;
    private boolean sameWriter;
    private WriterResponse writer;
    public DiaryDetailResponse(final Long id, final String title, final String text, final String image,
                               final LocalDateTime saveDate, final boolean sameWriter, final WriterResponse writer) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image = image;
        this.saveDate = saveDate;
        this.sameWriter = sameWriter;
        this.writer = writer;
    }

    public static DiaryDetailResponse of(final Diary diary, final Long memberId) {
        final WriterResponse writer = WriterResponse.from(diary.getMember());
        final boolean sameWriter = diary.getMember().isSameId(memberId);
        return new DiaryDetailResponse(diary.getId(), diary.getTitle(), diary.getText(), diary.getImage(),
                diary.getSaveDate(), sameWriter, writer);
    }

}
