package com.admiral.adminshop;

import com.admiral.common.dto.product.ProductCreateDTO;
import com.admiral.common.dto.product.ProductDTO;
import com.admiral.common.dto.product.ProductUpdateDTO;
import com.admiral.common.exception.ProductNotFoundException;
import com.admiral.common.service.ImageService;
import com.admiral.common.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureMockMvc
@Slf4j
@RequiredArgsConstructor
public class ProductServiceTests extends IntegrationTest {
    private static String IMAGE_BUCKET;

    private final ProductService productService;
    private final ImageService imageService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll(@Autowired Environment env) {
        IMAGE_BUCKET = env.getProperty("app.image.products.bucket");
        log.info("Image bucket: {}", IMAGE_BUCKET);
    }

    @AfterAll
    static void cleanImages() throws IOException {
        Path path = Paths.get(IMAGE_BUCKET);
        if (Files.exists(path)) {
            FileSystemUtils.deleteRecursively(path);
        }
    }

    @Test
    public void testGetAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        Assertions.assertNotNull(products);
        Assertions.assertFalse(products.isEmpty());
    }

    @Test
    public void testGetProductById() {
        ProductDTO product = productService.getProductById(1L);
        Assertions.assertNotNull(product);
        Assertions.assertEquals(1L, product.getId());
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductCreateDTO createDto = ProductCreateDTO.builder()
                .name("Test Product")
                .description("Description")
                .price(new BigDecimal("100.0"))
                .stockQuantity(10)
                .categoryName("Конструкторы")
                .build();
        var productJson = objectMapper.writeValueAsString(createDto);

        MockMultipartFile productPart = new MockMultipartFile(
                "product", "product", "application/json", productJson.getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                        .file(productPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    Assertions.assertNotNull(response);
                    ProductDTO created = objectMapper.readValue(response.getContentAsString(), ProductDTO.class);
                    assertThat(created.getName()).isEqualTo("Test Product");
                });
    }

    @Test
    public void testCreateProductWithImage() throws Exception {
        ProductCreateDTO createDto = ProductCreateDTO.builder()
                .name("Test Product Img")
                .description("Desc Img")
                .price(new BigDecimal("150.0"))
                .stockQuantity(5)
                .categoryName("Конструкторы")
                .build();
        var productJson = objectMapper.writeValueAsString(createDto);

        MockMultipartFile productPart = new MockMultipartFile(
                "product", "product", "application/json", productJson.getBytes()
        );
        MockMultipartFile imagePart = new MockMultipartFile(
                "image", "test-image.jpg", "image/jpeg", "fake-image-content".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                        .file(productPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    Assertions.assertNotNull(response);
                    ProductDTO created = objectMapper.readValue(response.getContentAsString(), ProductDTO.class);
                    assertThat(created.getName()).isEqualTo("Test Product Img");
                    assertThat(created.getImageId()).isEqualTo(1L);

                    ProductDTO productById = productService.getProductById(created.getId());
                    assertThat(productById).isNotNull();
                    assertThat(productById.getName()).isEqualTo(created.getName());

                    Optional<byte[]> imageById = imageService.getImageById(created.getImageId());
                    assertThat(imageById).isPresent();
                });
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductUpdateDTO updateDto = ProductUpdateDTO.builder()
                .id(1L)
                .name("Updated Name")
                .description("Updated Desc")
                .price(new BigDecimal("200.0"))
                .stockQuantity(20)
                .categoryName("Конструкторы")
                .build();
        var productJson = objectMapper.writeValueAsString(updateDto);

        MockMultipartFile productPart = new MockMultipartFile(
                "product", "product", "application/json", productJson.getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products/1")
                        .file(productPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    Assertions.assertNotNull(response);
                    ProductDTO updated = objectMapper.readValue(response.getContentAsString(), ProductDTO.class);
                    assertThat(updated.getName()).isEqualTo("Updated Name");
                });
    }

    @Test
    public void testUpdateProductWithImage() throws Exception {
        ProductUpdateDTO updateDto = ProductUpdateDTO.builder()
                .id(1L)
                .name("Updated Name Img")
                .description("Updated Desc Img")
                .price(new BigDecimal("250.0"))
                .stockQuantity(30)
                .categoryName("Конструкторы")
                .build();
        var productJson = objectMapper.writeValueAsString(updateDto);

        MockMultipartFile productPart = new MockMultipartFile(
                "product", "product", "application/json", productJson.getBytes()
        );
        MockMultipartFile imagePart = new MockMultipartFile(
                "image", "updated-image.jpg", "image/jpeg", "updated-fake-image-content".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products/1")
                        .file(productPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    Assertions.assertNotNull(response);
                    ProductDTO updated = objectMapper.readValue(response.getContentAsString(), ProductDTO.class);
                    assertThat(updated.getName()).isEqualTo("Updated Name Img");

                    ProductDTO productById = productService.getProductById(updated.getId());
                    assertThat(productById).isNotNull();
                    assertThat(productById.getName()).isEqualTo(updated.getName());

                    Optional<byte[]> imageById = imageService.getImageById(updated.getImageId());
                    assertThat(imageById).isPresent();
                });
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(result -> {
                    Long id = 1L;
                    assertThatThrownBy(() -> productService.getProductById(id))
                            .isInstanceOf(ProductNotFoundException.class)
                            .hasMessageContaining("Product not found with id: " + id);
                });
    }

} 