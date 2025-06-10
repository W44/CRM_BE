package com.crmw.CRM_BE.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/image")
public class ImageController {


    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/translate")
    //@CrossOrigin(origins = "*")
    public ResponseEntity<String> translateImageToText(@RequestParam("image") MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("No image uploaded.");
        }

        String translatedText = "Response from translation API";
        return ResponseEntity.ok(translatedText);
    }

}
