package com.admiral.common.database.repository;

import com.admiral.common.database.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select max(im.id) from Image im")
    Long findMaxId();
}
