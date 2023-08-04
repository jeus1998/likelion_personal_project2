package com.example.project_2_baejewoo.repository;

import com.example.project_2_baejewoo.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
}
