package com.project.secretdiary.dto.response;

import com.project.secretdiary.entity.MemberEntity;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponse {

    private Long id;
    private String userId;
    private String name;
    private String email;
    private String url;
    private String statusMessage;

    public FriendResponse(final MemberEntity member) {
        id = member.getId();
        userId = member.getUserId();
        name = member.getName();
        email = member.getEmail();
        url = member.getUrl();
        statusMessage = member.getStatusMessage();
    }
}
