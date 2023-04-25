package com.project.secretdiary.dto.response.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponse {

    private String image;

    private String statusMessage;

    public ProfileResponse(final String image, final String statusMessage) {
        this.image = image;
        this.statusMessage = statusMessage;
    }
}
