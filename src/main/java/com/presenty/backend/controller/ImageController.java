package com.presenty.backend.controller;

import com.presenty.backend.error.exception.InvalidImageException;
import com.presenty.backend.service.ImageService;
import com.presenty.backend.service.dto.CreateResult;
import com.presenty.backend.service.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<CreateResult<Long>> createImage(
            @RequestPart MultipartFile image) {
        validateMultipartFileImage(image);
        CreateResult<Long> createResult = imageService.saveImage(image.getResource());
        return ResponseEntity.ok(createResult);
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<Resource> getLocalImage(@PathVariable long imageId) throws IOException {
        ImageDto image = imageService.get(imageId);
        UrlResource resource = new UrlResource("file:" + image.getUrl());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(new File(image.getStoredName()).toPath()))
                .body(resource);
    }

    private void validateMultipartFileImage(MultipartFile image) {
        if (image.isEmpty() || !Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new InvalidImageException(String.format(
                    "isEmpty=%s, contentType=%s", image.isEmpty(), image.getContentType()));
        }
    }

}
