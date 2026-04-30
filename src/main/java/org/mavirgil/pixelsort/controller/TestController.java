package org.mavirgil.pixelsort.controller;

import org.mavirgil.pixelsort.config.ApiPath;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.API_BASE + "/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("connected");
    }
}
