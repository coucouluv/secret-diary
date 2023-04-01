package com.project.secretdiary.dto.response.friend;

import com.project.secretdiary.entity.Member;
import lombok.Getter;

@Getter
public class WaitingFriendResponse {
    private Long id;
    private String userId;
    private String name;

    private WaitingFriendResponse(final Long id, final String userId, final String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }

    public static WaitingFriendResponse from(final Member member) {
        return new WaitingFriendResponse(member.getId(), member.getUserId(), member.getName());
    }
}
