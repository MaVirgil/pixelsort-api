package org.mavirgil.pixelsort.controller;

import lombok.AllArgsConstructor;
import org.mavirgil.pixelsort.config.ApiPath;
import org.mavirgil.pixelsort.dto.Direction;
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

    private PixelsortService pixelsortService;

    @PostMapping("/sort")
    public ResponseEntity<byte[]> pixelsortImage(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || !originalFileName
                .toLowerCase(Locale.ROOT)
                .matches(".*\\.(jpg|jpeg|png)$")
        ) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println(Direction.UP);

        byte[] result = pixelsortService.process(file);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(result);
    }
}
