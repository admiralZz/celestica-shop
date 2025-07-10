package com.admiral.onlineshop.controller;

import com.admiral.common.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE) // такой медиа тайп для картинок
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return imageService.getImageById(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content)
                )
                .orElse(ResponseEntity.notFound().build());
    }
}
