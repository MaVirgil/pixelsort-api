package org.mavirgil.pixelsort.pixelsort;

import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class PixelSorter {

    public int[][] sort(int[][] imageData) {

        int[][] sortedArray = new int[imageData.length][imageData[0].length];
        int[][] mask = createMask(imageData, 60);

        for (int i = 0; i < imageData.length; i++){
            for (int j = 0; j < imageData[0].length; j++){

                //loop for comparison and swap
                for (int k = 0; k < imageData[i].length - j - 1; k++){

                    int[] rgb = colorToRgb(imageData[i][k]);
                    int[] rgbPlusOne = colorToRgb(imageData[i][k+1]);

                    if (mask[i][k] == -1) {
                        if (rgbToLuminance(rgb) > rgbToLuminance(rgbPlusOne)) {

                            int hold = imageData[i][k];
                            imageData[i][k] = imageData[i][k + 1];
                            imageData[i][k + 1] = hold;

                        }
                    }
                }
            }
        }

        return imageData;
    }

    public static int[][] createMask(int[][] imageArray, int clamp) {

        int[][] mask = new int[imageArray.length][imageArray[0].length];

        if (clamp > 255) clamp = 255;
        if (clamp < 0) clamp = 0;

        int minBrightness = 0 + clamp;
        int maxBrigthness = 255 - clamp;

        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {

                //convert pixel to RGB array
                int[] rgb = colorToRgb(imageArray[i][j]);

                //calulate percieved luminance of RGB array
                double luminance = rgbToLuminance(rgb);

                //mask out dark/bright spots
                if (luminance < maxBrigthness && luminance > minBrightness){
                    mask[i][j] = -1;
                } else mask[i][j] = -16777216;
            }
        }
        return mask;
    }

    public static int[] colorToRgb(int pixel){
        Color color = new Color (pixel, false);
        return new int[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    public static double rgbToLuminance(int[] pixel){
        return (0.2126 * pixel[0]) + (0.7152 * pixel[1]) + (0.0722 * pixel[2]);
    }

}
