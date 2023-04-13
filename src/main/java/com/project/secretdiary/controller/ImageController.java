package com.project.secretdiary.controller;

import com.project.secretdiary.dto.response.ImageResponse;
import com.project.secretdiary.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestPart MultipartFile file) {
        ImageResponse imageResponse = imageService.upload(file);
        return ResponseEntity.ok().body(imageResponse.getUrl());

    }

}
