package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.ArticleImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImageRepository extends JpaRepository<ArticleImagesEntity, Long> {
}
