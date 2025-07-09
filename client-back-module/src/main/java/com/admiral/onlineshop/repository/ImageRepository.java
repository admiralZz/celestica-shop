package com.admiral.onlineshop.repository;

import com.admiral.onlineshop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
