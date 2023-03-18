package com.project.secretdiary.dto.response;

import lombok.Getter;

@Getter
public class PreSignedResponse {

    String preSignedUrl;

    public PreSignedResponse(final String preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }
}
