package com.project.secretdiary.dto.response.member;

import com.project.secretdiary.entity.Member;
import lombok.Getter;

@Getter
public class WriterResponse {
    private Long id;
    private String userId;
    private String image;

    public WriterResponse(final Long id, final String userId, final String image) {
        this.id = id;
        this.userId = userId;
        this.image = image;
    }

    public static WriterResponse from(final Member member) {
        return new WriterResponse(member.getId(), member.getUserId(), member.getImage());
    }
}
