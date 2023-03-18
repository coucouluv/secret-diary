package com.project.secretdiary.dto.response;

import lombok.Getter;

@Getter
public class UploadPreSignedResponse {

    String url;
    String preSignedUrl;

    public UploadPreSignedResponse(final String url, final String preSignedUrl) {
        this.url = url;
        this.preSignedUrl = preSignedUrl;
    }
}
