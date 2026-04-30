package org.mavirgil.pixelsort.controller;

import org.mavirgil.pixelsort.config.ApiPath;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.IMAGE_BASE)
@CrossOrigin("*")
public class ImageController {

    @GetMapping(ApiPath.ID)
    public ResponseEntity<String> test (@PathVariable String id) {
        return ResponseEntity.ok("id: " + id);
    }
}
