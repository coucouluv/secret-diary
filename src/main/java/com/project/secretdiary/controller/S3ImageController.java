package com.project.secretdiary.controller;

import com.project.secretdiary.dto.response.PreSignedResponse;
import com.project.secretdiary.dto.response.UploadPreSignedResponse;
import com.project.secretdiary.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class S3ImageController {

    private final S3ImageService s3ImageService;

    @GetMapping("/share/{url}")
    public ResponseEntity<PreSignedResponse> share(@PathVariable("url") final String url) {
        PreSignedResponse preSignedResponse = s3ImageService.createSharePreSignedUrl(url);
        return ResponseEntity.ok(preSignedResponse);
    }

    @GetMapping("/upload")
    public ResponseEntity<UploadPreSignedResponse> upload() {
        UploadPreSignedResponse uploadPreSignedResponse = s3ImageService.createUploadPreSignedUrl();
        return ResponseEntity.ok(uploadPreSignedResponse);
    }

    @GetMapping("/delete/{url}")
    public ResponseEntity<PreSignedResponse> delete(@PathVariable("url") final String url) {
        PreSignedResponse preSignedResponse = s3ImageService.createDeletePreSignedUrl(url);
        return ResponseEntity.ok(preSignedResponse);
    }
}
