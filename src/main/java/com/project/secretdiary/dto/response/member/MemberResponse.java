package com.project.secretdiary.dto.response.member;

import com.project.secretdiary.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponse {
    private String userId;
    private String name;
    private String email;
    private String statusMessage;
    private String image;

    private MemberResponse(final String userId, final String name, final String email,
                          final String statusMessage, final String image) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.statusMessage = statusMessage;
        this.image = image;
    }

    public static MemberResponse of(final Member member) {
        return new MemberResponse(member.getUserId(), member.getName(), member.getEmail(),
                member.getStatusMessage(), member.getImage());
    }
}
