package com.project.secretdiary.util;

import com.project.secretdiary.exception.FileIOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class ImageCompress {

    public static void compress(File originFile, File compressFile, String extension) {
        try {
            OutputStream outputStream = new FileOutputStream(compressFile);
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);

            BufferedImage bufferedImage = getBufferedImage(originFile);
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension);
            ImageWriter writer = writers.next();
            writer.setOutput(imageOutputStream);

            ImageWriteParam defaultWriteParam = writer.getDefaultWriteParam();
            if(defaultWriteParam.canWriteCompressed()) {
                defaultWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                defaultWriteParam.setCompressionQuality(0.5f);
            }
            writer.write(null, new IIOImage(bufferedImage, null, null), defaultWriteParam);

        } catch (Exception e) {
            throw new FileIOException("이미지 압축 시 오류가 발생했습니다.");
        }
        originFile.delete();
    }

    public static BufferedImage getBufferedImage(File originFile) {

        try {
            BufferedImage originalImage = ImageIO.read(originFile);

            BufferedImage bufferedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            graphics2D.drawImage(originalImage, 0, 0, null);
            graphics2D.dispose();

            return bufferedImage;
        } catch(Exception e) {
            throw new FileIOException("이미지 파일을 읽을 때 오류가 발생했습니다.");
        }

    }
}
