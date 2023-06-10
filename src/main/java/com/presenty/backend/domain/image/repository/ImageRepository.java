package com.presenty.backend.domain.image.repository;

import com.presenty.backend.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
