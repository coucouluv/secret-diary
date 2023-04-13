package com.project.secretdiary.entity;

import java.util.UUID;

public class Image {

    private final String path;
    private final ImageExtension extension;
    private final String name;

    public Image(final String path, final ImageExtension extension, final String name) {
        this.path = path;
        this.extension = extension;
        this.name = name;
    }

    public static Image of(final String path, final String extensionName) {
        ImageExtension imageExtension = ImageExtension.from(extensionName);
        String name = UUID.randomUUID() + "." + imageExtension.getExtensionName();
        return new Image(path, imageExtension, name);
    }

    public String getFullPath() {
        return path+name;
    }

    public String getCompressPath() {
        String compressName = "compress-" + name;
        return path+compressName;
    }

    public String getName() {
        return name;
    }

    public ImageExtension getExtension() {
        return extension;
    }

    public boolean isNotCompress() {
        if(extension == ImageExtension.GIF) {
            return true;
        }
        return false;
    }

}
