package org.mavirgil.pixelsort.controller;

import lombok.AllArgsConstructor;
import org.mavirgil.pixelsort.config.ApiPath;
import org.mavirgil.pixelsort.sorting.SortOrder;
import org.mavirgil.pixelsort.sorting.SortOrientation;
import org.mavirgil.pixelsort.sorting.PixelsortOptions;
import org.mavirgil.pixelsort.service.PixelsortService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@RestController
@RequestMapping(ApiPath.IMAGE_BASE)
@CrossOrigin("*")
@AllArgsConstructor
public class ImageController {

    private final PixelsortService pixelsortService;

    @PostMapping("/sort")
    public ResponseEntity<byte[]> pixelsortImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "40") int lowerThreshold,
            @RequestParam(defaultValue = "210") int upperThreshold,
            @RequestParam(defaultValue = "2") int minSegmentLength,
            @RequestParam(defaultValue = "HORIZONTAL") SortOrientation sortOrientation,
            @RequestParam(defaultValue = "ASCENDING") SortOrder sortOrder
    ) {

        if (!isValidFile(file)) {
            return ResponseEntity.badRequest().build();
        }

        PixelsortOptions options = new PixelsortOptions(sortOrientation, sortOrder, lowerThreshold, upperThreshold, minSegmentLength);

        byte[] result = pixelsortService.process(file, options);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(result);
    }

    private boolean isValidFile(MultipartFile file) {

        if (file.isEmpty()) {
            return false;
        }

        String originalFileName = file.getOriginalFilename();

         return originalFileName == null || !originalFileName
                .toLowerCase(Locale.ROOT)
                .matches(".*\\.(jpg|jpeg|png)$");
    }
}
