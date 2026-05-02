package org.mavirgil.pixelsort.util;

import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class RgbTools {

    public int[] colorToRgb(int pixel){
        Color color = new Color (pixel, false);
        return new int[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    public double rgbToLuminance(int[] pixel){
        return (0.2126 * pixel[0]) + (0.7152 * pixel[1]) + (0.0722 * pixel[2]);
    }
}
