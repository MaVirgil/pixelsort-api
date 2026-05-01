package org.mavirgil.pixelsort.service;

import lombok.AllArgsConstructor;
import org.mavirgil.pixelsort.pixelsort.PixelSorter;
import org.mavirgil.pixelsort.util.ImageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class PixelsortService {

    private ImageConverter imageConverter;

    private PixelSorter pixelSorter;

    public byte[] process(MultipartFile file) {

        try {
            byte[] byteArr = file.getBytes();

            int[][] sorted = pixelSorter.sort(imageConverter.byteArrayToRgbArray(byteArr));


            return imageConverter.rgbArrayToByteArray(sorted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
