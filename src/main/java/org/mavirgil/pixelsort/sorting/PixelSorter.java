package org.mavirgil.pixelsort.sorting;

import lombok.AllArgsConstructor;
import org.mavirgil.pixelsort.util.ImageTransformer;
import org.mavirgil.pixelsort.util.RgbTools;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@AllArgsConstructor
public class PixelSorter {

    private final RgbTools rgbTools;
    private final ImageTransformer imageTransformer;

    private static final int WHITE_ARGB = 0xFFFFFFFF;
    private static final int BLACK_ARGB = 0xFF000000;



    public int[][] sort(int[][] imageData, PixelsortOptions options) {

        //unwrap options
        SortOrientation sortOrientation = options.sortOrientation();
        SortOrder sortOrder = options.sortOrder();
        int lowerThreshold = options.lowerThreshold();
        int upperThreshold = options.upperThreshold();
        int minSegmentLength = options.minSegmentLength();

        if (sortOrientation == SortOrientation.VERTICAL) {
            imageData = imageTransformer.rotateCw(imageData);
            imageData = sort(imageData, lowerThreshold, upperThreshold, minSegmentLength, sortOrder);
            return imageTransformer.rotateCcw(imageData);
        } else {
            return sort(imageData, lowerThreshold, upperThreshold, minSegmentLength, sortOrder);
        }
    }

    private int[][] sort(int[][] imageData, int lowerThreshold, int upperThreshold, int minSegmentLength, SortOrder sortOrder) {

        if (minSegmentLength < 2 || minSegmentLength > imageData[0].length) {
            throw new IllegalArgumentException("minSegmentLength must be equal to or above 2 and below or equal to width of imageData");
        }

        Comparator<KeyedPixel> comparator = Comparator.comparingDouble(KeyedPixel::sortKey);

        if (sortOrder == SortOrder.DESCENDING) {
            comparator = comparator.reversed();
        }

        int[][] mask = getMask(imageData, lowerThreshold, upperThreshold);
        int[][] resultImageData = new int[imageData.length][imageData[0].length];

        //Loop through imageData and find series of pixels within mask
        for (int i = 0; i < resultImageData.length; i++) {

            List<KeyedPixel> pixelSegment = new ArrayList<>();
            int runStartingIndex = 0;

            for (int j = 0; j < resultImageData[i].length; j++) {

                //if pixel is within mask, add it
                if (mask[i][j] == WHITE_ARGB) {

                    if (pixelSegment.isEmpty()) {
                        runStartingIndex = j;
                    }

                    pixelSegment.add(toKeyedPixel(imageData[i][j]));

                    continue;
                }

                //Sort segment if size is above or equal to minimum sorting
                processSegment(pixelSegment, comparator, minSegmentLength, resultImageData, i, runStartingIndex);

                resultImageData[i][j] = imageData[i][j];
                pixelSegment.clear();
            }

            processSegment(pixelSegment, comparator, minSegmentLength, resultImageData, i, runStartingIndex);
        }
        return resultImageData;
    }

    /**
     *Creates a mask from the given image based on perceived pixel luminance. Pixels within the range are set to solid white,
     * while pixels outside the range are set to solid black.
     *
     * @param imageArray the source image as a 2D array of RGB pixel values
     * @param lowerThreshold the inclusive lower threshold, in the range {@code [0, 255]}
     * @param upperThreshold the inclusive upper threshold, in the range {@code [0, 255]}
     * @return a 2D mask array with the same dimensions as input {@code imageArray},
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
            throw new IllegalArgumentException("lowerThreshold must be less than or equal to upperThreshold");
        }

        int[][] mask = new int[imageArray.length][imageArray[0].length];

        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[i].length; j++) {

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

    private KeyedPixel toKeyedPixel(int pixelValue) {
        int[] rgbData = rgbTools.colorToRgb(pixelValue);
        return new KeyedPixel(pixelValue, rgbTools.rgbToLuminance(rgbData));
    }

    private void processSegment(
            List<KeyedPixel> pixelSegment,
            Comparator<KeyedPixel> comparator,
            int minSortingLength,
            int[][] sortedImage,
            int rowIndex,
            int runStartingIndex
    ) {
        if (pixelSegment.size() >= minSortingLength) {
            pixelSegment.sort(comparator);
        }

        if (!pixelSegment.isEmpty()) {
            insertPixels(sortedImage, pixelSegment, rowIndex, runStartingIndex);
        }
    }

    private void insertPixels(int[][] target, List<KeyedPixel> toInsert, int i, int runStartingIndex) {
        for (int k = 0; k < toInsert.size(); k++) {
            target[i][runStartingIndex + k] = toInsert.get(k).pixelValue();
        }
    }
}
