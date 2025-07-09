package com.admiral.onlineshop.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageService {

    void upload(MultipartFile file, String imageName);
    Optional<byte[]> getImageById(Long id);
}
