package com.project.secretdiary.service;

import com.project.secretdiary.dto.response.ImageResponse;
import com.project.secretdiary.exception.InvalidExtensionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class ImageServiceTest {
    @Autowired
    private ImageService imageService;

    private byte[] toByteArray(final BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Test
    @DisplayName("이미지 업로드 성공한다.")
    public void 이미지_업로드_성공() throws Exception {
        //given
        MockMultipartFile jpgFile = new MockMultipartFile("file",
                "test.jpg",
                "multipart/form-data",
                toByteArray(new BufferedImage(100, 100, TYPE_INT_RGB)));
        //when
        ImageResponse imageResponse = imageService.upload(jpgFile);
        //then
        assertThat(imageResponse.getUrl()).startsWith("compress");
    }

    @Test
    @DisplayName("gif 파일은 압축하지 않고 업로드 한다.")
    public void git_파일_업로드_성공() throws Exception {
        //given
        MockMultipartFile gifFile = new MockMultipartFile("file",
                "test.gif",
                "multipart/form-data",
                toByteArray(new BufferedImage(100, 100, TYPE_INT_RGB)));
        //when
        ImageResponse imageResponse = imageService.upload(gifFile);
        //then
        assertThat(imageResponse.getUrl()).doesNotStartWith("compress");
    }

    @Test
    @DisplayName("파일을 입력하지 않고 업로드하려고 할때 예외가 발생한다.")
    public void 파일_입력하지_않고_업로드시_예외_발생() throws Exception {
        //given
        MockMultipartFile notExistFile = new MockMultipartFile("file", InputStream.nullInputStream());

        //when //then
        assertThatThrownBy(() -> imageService.upload(notExistFile))
                .isInstanceOf(InvalidExtensionException.class);
    }
}