package com.admiral.common.service;

import com.admiral.common.conf.image.ProductImageConfigProperties;
import com.admiral.common.database.model.Image;
import com.admiral.common.database.repository.ImageRepository;
import com.admiral.common.exception.ImageUploadingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductImageService implements ImageService {

    private final ProductImageConfigProperties properties;
    private final ImageRepository imageRepository;

    public ProductImageService(ImageRepository imageRepository,
                               ProductImageConfigProperties properties) {
        this.imageRepository = imageRepository;
        this.properties = properties;
        log.info("Using image bucket for products: {}", properties.bucket());
        log.info("Using image file prefix for products: {}", properties.prefixFileName());
    }

    @Transactional
    public Image upload(MultipartFile file) {
        return upload(file, generateImageName());
    }

    @SneakyThrows // превращает checked exception в RuntimeException(т.е. пробрасывает выше)
    @Transactional
    @Override
    public Image upload(MultipartFile file, String imageName) {
        Path path = retrievePathToImageWithExtension(file, imageName);

        try (InputStream content = file.getInputStream()) {
            // Если такой папки не существует
            Files.createDirectories(path.getParent());
            Files.write(path, content.readAllBytes(),
                    // Создать если не существует и перезаписать если существует
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return imageRepository.save(Image.builder()
                    .name(path.getFileName().toString())
                    .build());
        } catch (IOException e) {
            throw new ImageUploadingException(e.getMessage(), e);
        }
    }

    @SneakyThrows
    @Override
    public Optional<byte[]> getImageById(Long id) {
        log.debug("Retrieving image by id: {}", id);
        Optional<Image> optionalImage = imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            Path path = Path.of(properties.bucket(), image.getName());
            log.debug("Found image by path: {}", path);

            if (Files.exists(path)) {
                return Optional.of(Files.readAllBytes(path));
            }
        }

        return Optional.empty();
    }

    @Override
    public String generateImageName() {
        Long maxId = imageRepository.findMaxId();
        if (maxId == null) {
            maxId = 0L;
        }
        return String.format(properties.prefixFileName() +"%d", maxId + 1);
    }

    private Path retrievePathToImageWithExtension(MultipartFile file, String imageName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image cannot be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("Cannot determine content type of uploaded file");
        }

        // Получаем MIME и расширение
        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Unsupported image type: " + file.getContentType());
        };
        return Path.of(properties.bucket(), imageName + extension);
    }
}
