package com.project.secretdiary.service;

import com.project.secretdiary.dto.response.ImageResponse;
import com.project.secretdiary.entity.Image;
import com.project.secretdiary.exception.FileIOException;
import com.project.secretdiary.util.ImageCompress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.springframework.util.StringUtils.getFilenameExtension;

@Service
public class ImageService {

    @Value("${image.dir}")
    private String imageDir;

    public ImageResponse upload(final MultipartFile multipartFile) {
        Image image = Image.of(imageDir, getFilenameExtension(multipartFile.getOriginalFilename()));
        save(image, multipartFile);
        if (image.isNotCompress()) {
            return new ImageResponse(image.getName());
        }

        String compressImage = compress(image);
        return new ImageResponse(compressImage);
    }

    private String compress(final Image image) {
        File originFile = new File(image.getFullPath());
        File compressFile = new File(image.getCompressPath());
        ImageCompress.compress(originFile, compressFile, image.getExtension().getExtensionName());
        return compressFile.getName();
    }

    private void save(final Image image, final MultipartFile multipartFile) {
        try {
            File file = new File(image.getFullPath());
            multipartFile.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileIOException();
        }
    }

}
