package com.project.secretdiary.dto.response.friend;

import com.project.secretdiary.entity.MemberEntity;
import lombok.*;

@Getter
@NoArgsConstructor
public class FriendResponse {

    private Long id;
    private String userId;
    private String name;
    private String email;
    private String url;
    private String statusMessage;

    private FriendResponse(final Long id, final String userId, final String name, final String email,
                          final String url, final String statusMessage) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.url = url;
        this.statusMessage = statusMessage;
    }

    public static FriendResponse from(final MemberEntity member) {
        return new FriendResponse(member.getId(), member.getUserId(), member.getName(), member.getEmail(),
                member.getUrl(), member.getStatusMessage());
    }

}
