package com.project.secretdiary.dto.request.diary;

import com.project.secretdiary.entity.Diary;
import com.project.secretdiary.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequest {

    @NotNull(message = "친구 아이디를 입력하세요.")
    private Long id;

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String text;

    private String image;

    public Diary toDiary(final Member member, final Member friend) {
        return Diary.builder()
                .title(title)
                .text(text)
                .image(image)
                .saveDate(LocalDateTime.now())
                .member(member)
                .friend(friend)
                .build();
    }

}
