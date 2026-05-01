package org.mavirgil.pixelsort.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Component
public class ImageConverter {

    public int[][] byteArrayToRgbArray(byte[] bytes) {

        BufferedImage image = byteArrayToBufferedImage(bytes);

        //convert buffered image to 2D array
        int[][] imageArray = new int[image.getHeight()][image.getWidth()];

        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[0].length; j++) {
                imageArray[i][j] = image.getRGB(j, i);
            }
        }

        return imageArray;
    }

    public byte[] rgbArrayToByteArray(int[][] rgbArray) {

        int imageHeight = rgbArray.length;
        int imageWidth = rgbArray[0].length;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < rgbArray.length; i++) {
            for (int j = 0; j < rgbArray[0].length; j++) {
                image.setRGB(j, i, rgbArray[i][j]);
            }
        }

        return bufferedImageToByteArray(image);
    }

    private BufferedImage byteArrayToBufferedImage(byte[] bytes) {

        BufferedImage image;

        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);

            image = ImageIO.read(inputStream);

            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] bufferedImageToByteArray(BufferedImage image) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            ImageIO.write(image, "png", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
