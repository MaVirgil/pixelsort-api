package org.mavirgil.pixelsort.pixelsort;

import lombok.AllArgsConstructor;
import org.mavirgil.pixelsort.util.RgbTools;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PixelSorter {

    private RgbTools rgbTools;

    public int[][] sort(int[][] imageData, int lowerThreshold, int upperThreshold) {

        int[][] mask = getMask(imageData, lowerThreshold, upperThreshold);

        System.out.println("Sorting image with resolution: " + imageData.length + "x" + imageData[0].length);

        for (int i = 0; i < imageData.length; i++){
            for (int j = 0; j < imageData[0].length; j++){

                //loop for comparison and swap
                for (int k = 0; k < imageData[i].length - j - 1; k++){

                    int[] rgb = rgbTools.colorToRgb(imageData[i][k]);
                    int[] rgbPlusOne = rgbTools.colorToRgb(imageData[i][k+1]);

                    if (mask[i][k] == -1) {
                        if (rgbTools.rgbToLuminance(rgb) > rgbTools.rgbToLuminance(rgbPlusOne)) {

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

    /**
     *Creates a mask from the given image based on perceived pixel luminance. Pixels that fall within the range are white,
     * while pixels outside the range will be black.
     *
     * @param imageArray the source image as a 2D array of ARGB pixel values
     * @param lowerThreshold the inclusive lower threshold, in the range {@code [0, 255]}
     * @param upperThreshold the inclusive upper threshold, in the range {@code [0, 255]}
     * @return a 2D mask array with the same dimension as input {@code imageArray},
     *      containing white pixels for values inside the threshold range, and black pixels
     *      for values outside it.
     * @throws IllegalArgumentException if {@code lowerThreshold} or {@code upperThreshold} is outside
     *      the range {@code [0, 255]}, or if {@code lowerThreshold > upperThreshold}
     */
    public int[][] getMask(int[][] imageArray, int lowerThreshold, int upperThreshold) {

        if (lowerThreshold < 0 || lowerThreshold > 255) {
            throw new IllegalArgumentException("lowerThreshold must be between 0 and 255");
        }

        if (upperThreshold < 0 || upperThreshold > 255) {
            throw new IllegalArgumentException("upperThreshold must be between 0 and 255");
        }

        if (lowerThreshold > upperThreshold) {
            throw new IllegalArgumentException("lowerThreshold must be less or equal to upperThreshold");
        }

        int[][] mask = new int[imageArray.length][imageArray[0].length];

        final int WHITE_ARGB = 0xFFFFFFFF;
        final int BLACK_ARGB = 0xFF000000;

        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {

                //convert pixel to RGB array
                int[] rgb = rgbTools.colorToRgb(imageArray[i][j]);

                //calculate perceived luminance of RGB array
                double luminance = rgbTools.rgbToLuminance(rgb);

                //mask out dark/bright spots
                if (luminance <= upperThreshold && luminance >= lowerThreshold){
                    mask[i][j] = WHITE_ARGB;
                } else mask[i][j] = BLACK_ARGB;
            }
        }
        return mask;
    }
}
