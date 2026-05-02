package org.mavirgil.pixelsort.service;

import lombok.AllArgsConstructor;
import org.mavirgil.pixelsort.sorting.PixelsortOptions;
import org.mavirgil.pixelsort.sorting.PixelSorter;
import org.mavirgil.pixelsort.util.ImageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class PixelsortService {

    private ImageConverter imageConverter;

    private PixelSorter pixelSorter;

    public byte[] process(MultipartFile file, PixelsortOptions options) {

        byte[] byteArr;

        try {
            byteArr  = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }

        int[][] imageData = imageConverter.byteArrayToRgbArray(byteArr);

        int[][] sorted = pixelSorter.sort(imageData, options);

        return imageConverter.rgbArrayToByteArray(sorted);
    }
}
