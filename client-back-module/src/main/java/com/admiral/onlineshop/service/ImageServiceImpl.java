package com.admiral.onlineshop.service;

import com.admiral.onlineshop.model.Image;
import com.admiral.onlineshop.repository.ImageRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final String bucket;

    public ImageServiceImpl(ImageRepository imageRepository,
                            @Value("${app.image.bucket.products:/tmp}") String bucket) {
        this.imageRepository = imageRepository;
        this.bucket = bucket;
        log.info("Using image bucket: {}", bucket);
    }

    @Override
    public void upload(MultipartFile file, String imageName) {

    }

    @SneakyThrows
    @Override
    public Optional<byte[]> getImageById(Long id) {
        Optional<Image> optionalImage = imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            Path path = Path.of(bucket, image.getName());

            if (Files.exists(path)) {
                return Optional.of(Files.readAllBytes(path));
            }
        }

        return Optional.empty();
    }
}
