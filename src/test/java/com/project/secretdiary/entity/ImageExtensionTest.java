package com.project.secretdiary.entity;

import com.project.secretdiary.exception.InvalidExtensionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ImageExtensionTest {

    @ParameterizedTest
    @ValueSource(strings = {"pdf", "svg", "txt", "mp4"})
    @DisplayName("지원하지 않는 확장자를 입력했을 때 예외가 발생한다.")
    public void 지원하지_않는_확장자_입력시_예외_발생() throws Exception {
        //given
        //when
        //then
        assertThatThrownBy(() -> ImageExtension.from("pdf"))
                .isInstanceOf(InvalidExtensionException.class)
                .hasMessage("지원하지 않은 확장자입니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"png,PNG", "jpg,JPG", "jpeg,JPEG", "gif,GIF"})
    @DisplayName("올바른 확장자를 입력했을 때 예외가 발생하지 않는다.")
    public void 올바른_확장자_입력시_올바른_값_반환(String extensionName, ImageExtension extension) throws Exception {
        //given
        //when
        ImageExtension imageExtension = ImageExtension.from(extensionName);
        //then
        assertThat(imageExtension).isEqualTo(extension);
    }
}