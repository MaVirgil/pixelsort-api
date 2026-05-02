package org.mavirgil.pixelsort.util;

import org.springframework.stereotype.Component;

@Component
public class ImageTransformer {

    public int[][] rotateCw(int[][] imageArray){

        int[][] rotatedArray = new int[imageArray[0].length][imageArray.length];

        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[0].length; j++) {
                rotatedArray[j][(rotatedArray[0].length) - 1 - i] = imageArray[i][j];
            }
        }

        return rotatedArray;
    }

    public int[][] rotateCcw(int[][] imageArray){

        int[][] rotatedArray = new int[imageArray[0].length][imageArray.length];

        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[0].length; j++) {
                rotatedArray[(rotatedArray.length) - 1 - j][i] = imageArray[i][j];
            }
        }

        return rotatedArray;
    }
}
