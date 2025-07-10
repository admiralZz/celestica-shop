package com.admiral.common.service;

import com.admiral.common.database.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageService {

    Image upload(MultipartFile file, String imageName);
    Optional<byte[]> getImageById(Long id);
    String generateImageName();
}
