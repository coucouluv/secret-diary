package com.project.secretdiary.service;

import com.amazonaws.HttpMethod;
import com.project.secretdiary.dto.response.PreSignedResponse;
import com.project.secretdiary.dto.response.UploadPreSignedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageService {

    private final ImageUploader imageUploader;

    public PreSignedResponse createSharePreSignedUrl(String url) {
        String preSignedUrl = imageUploader.createPreSignedUrl(url, HttpMethod.GET);
        return new PreSignedResponse(preSignedUrl);
    }

    public UploadPreSignedResponse createUploadPreSignedUrl() {
        String url = UUID.randomUUID() + ".jpg";
        String preSignedUrl = imageUploader.createPreSignedUrl(url, HttpMethod.PUT);
        return new UploadPreSignedResponse(url, preSignedUrl);
    }

    public PreSignedResponse createDeletePreSignedUrl(String url) {
        String preSignedUrl = imageUploader.createPreSignedUrl(url, HttpMethod.DELETE);
        return new PreSignedResponse(preSignedUrl);
    }
}
