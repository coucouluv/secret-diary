package com.project.secretdiary.entity;

import com.project.secretdiary.exception.InvalidExtensionException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ImageExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif");

    private final String extensionName;

    ImageExtension(String extensionName) {
        this.extensionName = extensionName;
    }

    public static ImageExtension from(String extensionName) {
        return Arrays.stream(values())
                .filter(value -> value.extensionName.equals(extensionName))
                .findAny()
                .orElseThrow(InvalidExtensionException::new);
    }
}
