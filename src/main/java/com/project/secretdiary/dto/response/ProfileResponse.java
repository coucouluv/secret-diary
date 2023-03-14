package com.project.secretdiary.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponse {

    private String url;

    private String statusMessage;

    public ProfileResponse(final String url, final String statusMessage) {
        this.url = url;
        this.statusMessage = statusMessage;
    }
}
