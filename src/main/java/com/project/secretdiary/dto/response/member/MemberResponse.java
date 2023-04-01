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
    private String url;

    private MemberResponse(final String userId, final String name, final String email,
                          final String statusMessage, final String url) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.statusMessage = statusMessage;
        this.url = url;
    }

    public static MemberResponse of(final Member member, final String preSignedUrl) {
        return new MemberResponse(member.getUserId(), member.getName(), member.getEmail(),
                member.getStatusMessage(), preSignedUrl);
    }
}
